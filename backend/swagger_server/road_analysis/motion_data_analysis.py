from swagger_server import constants as const, mongodb_interface
import pandas as pd
import os
import datetime
import logging
# import tkinter
import matplotlib.pyplot as plt
from math import radians, cos, sin, asin, sqrt
import random
import numpy as np
import time


def haversine(lon1, lat1, lon2, lat2):
    """
    Calculate the great circle distance between two points on the earth (specified in decimal degrees)
    """
    # convert decimal degrees to radians
    lon1, lat1, lon2, lat2 = map(radians, [lon1, lat1, lon2, lat2])
    # haversine formula
    dlon = lon2 - lon1
    dlat = lat2 - lat1
    a = sin(dlat / 2) ** 2 + cos(lat1) * cos(lat2) * sin(dlon / 2) ** 2
    c = 2 * asin(sqrt(a))
    r = 6371  # Radius of earth in kilometers. Use 3956 for miles
    return c * r


def plot(motion_df, bumps):
    """
    Plot motion data signals
    """
    plt.plot(motion_df['accelerometerZ'], label='acc_z')
    plt.axhline(y=2*motion_df['accelerometerZ'].mean(), label='2x', color='r', linestyle='-')
    # plt.plot(motion_df['acc_y'], label='acc_y')
    # plt.plot(motion_df['mag_x'], label='mag_x')
    # plt.plot(motion_df['mag_y'], label='mag_y')
    # plt.plot(motion_df['mag_z'], label='mag_z')
    # plt.plot(motion_df['gyr_x'], label='gyr_x')
    # plt.plot(motion_df['gyr_y'], label='gyr_y')
    # plt.plot(motion_df['gyr_z'], label='gyr_z')
    plt.legend(bbox_to_anchor=(1.05, 1), loc='upper left', borderaxespad=0.)
    plt.show()


def retrieve_data(trip_uuid):
    """
    Retrieve gnss data and motion data from mongodb
    """

    logging.info('Start looking for trip data...')
    gnss_data = None
    dead = False
    time_to_live = const.TIME_TO_LIVE
    while gnss_data is None:
        if time_to_live == 0:
            dead = True
            break
        try:
            gnss_data = pd.DataFrame(mongodb_interface.get_trip_by_trip_uuid(trip_uuid)['gnss_data'])
            logging.info(gnss_data.__str__())
        except Exception as ex:
            logging.info('Still looking...')
            pass
        time.sleep(const.DELAY)
        time_to_live -= 1
    if dead:
        return None, None, "Data retrieval failed due to timeout"

    if len(gnss_data.index) == 1:
        return None, None, "Trip with only one GNSS point"

    # ping for motion data until found
    logging.info('Start looking for motion file...')
    motion_file = None
    dead = False
    time_to_live = const.TIME_TO_LIVE
    while motion_file is None:
        if time_to_live == 0:
            dead = True
            break
        try:
            motion_file = mongodb_interface.get_file_by_filename(trip_uuid)
        except Exception as ex:
            logging.info('Still looking...')
            pass
        time.sleep(const.DELAY)
        time_to_live -= 1
    if dead:
        return None, None, "Data retrieval failed due to timeout"
    else:
        log = 'MOTION DATA ANALYSIS OF TRIP %s\n' % trip_uuid
        log += 'First timestamp of trip is: %s\n' % gnss_data.head(1)['time_ts']
        log += 'Last timestamp of trip is: %s\n' % gnss_data.tail(1)['time_ts']

    # motion dataframe creation
    # TODO find a way to read grid file into dataframe without a temporary file
    # this is horrible!
    with open("tmp.csv", "w") as fp:
        fp.write(list(motion_file)[0].decode('utf-8'))
    with open("tmp.csv", "r") as fp:
        motion_df = pd.read_csv(fp)
    os.remove("tmp.csv")
    # TODO DEBUG ONLY there's an error in last row of the motion file I'm using, so I remove it
    # motion_df = motion_df.head(-1)
    motion_df['timestamp'] = motion_df['timestamp'].apply(datetime.datetime.strptime, args=['%Y-%m-%dT%H:%M:%SZ'])
    log += 'First timestamp of motion data is: %s\n' % motion_df.iloc[0]['timestamp']
    log += 'Last timestamp of motion data is: %s\n' % motion_df.iloc[-1]['timestamp']

    return gnss_data, motion_df, log


# for each point into trip data, take the chunk of motion data marked with the same timestamp
# describe() shows main statistics available for each chunk
def calculate_road_quality(trip_uuid, gnss_data, motion_df):
    log = 'MOTION DATA SUMMARY DESCRIPTION\n'
    log += str(motion_df.describe())
    road_quality = []
    m_accZ = motion_df['accelerometerZ'].mean()
    # time_ts is used as a primary key over gnss
    for index, ts in enumerate(gnss_data['time_ts']):
        if index == len(gnss_data['time_ts']) - 1:
            # road quality is calculated per each gnss point except the last one
            pass
        # chunk is the subset of motion data registered in the time interval of current gnss
        chunk = motion_df.loc[motion_df['timestamp'] == ts]
        # calculate road quality
        r2 = 0
        for accZ in chunk['accelerometerZ']:
            r2 += (m_accZ - accZ) ** 2
        road_quality.append(r2)
    normalized_road_quality = []
    if np.max(road_quality) - np.min(road_quality) != 0:
        normalized_road_quality = (road_quality - np.min(road_quality)) / (np.max(road_quality) - np.min(road_quality))
    #print(normalized_road_quality)
    # switch from road badness to road goodness
    # normalized_road_quality = 1 - normalized_road_quality
    for index, chunk_road_quality in enumerate(normalized_road_quality):
        mongodb_interface.update_trip_road_quality(trip_uuid, index, chunk_road_quality)
    mongodb_interface.update_road_quality_statistics(trip_uuid, normalized_road_quality.min() * 100,
                                                     normalized_road_quality.max() * 100,
                                                     normalized_road_quality.mean() * 100)
    coords = [[gp['lon'], gp['lat']] for gp in mongodb_interface.get_trip_by_trip_uuid(trip_uuid)['gnss_data']]

    # removing any possible NaN from coming out of here
    def nan_cleaner(el):
        if np.isnan(el):
            return None
        return el

    track = {'loc': {'type': 'LineString', 'coordinates': coords},
             'quality_scores': list(map(nan_cleaner, road_quality))}
    return track, log


def calculate_bumps(trip_uuid, gnss_data, motion_df):
    bumpy_scores = []
    m_acc_z = motion_df['accelerometerZ'].mean()
    for index, gnss in gnss_data.iterrows():
        chunk = motion_df.loc[motion_df['timestamp'] == gnss['time_ts']]
        for i, record in chunk.iterrows():
            curr_acc_z = record['accelerometerZ']
            if curr_acc_z > const.BUMPS_THRESHOLDS[0] * m_acc_z:
                bumpy_score = None
                lon = None
                lat = None
                if const.BUMPS_THRESHOLDS[0] * m_acc_z < curr_acc_z <= const.BUMPS_THRESHOLDS[1] * m_acc_z:
                    bumpy_score, lon, lat = 1, gnss['lon'], gnss['lat']
                elif const.BUMPS_THRESHOLDS[1] * m_acc_z < curr_acc_z <= const.BUMPS_THRESHOLDS[2] * m_acc_z:
                    bumpy_score, lon, lat = 2, gnss['lon'], gnss['lat']
                elif const.BUMPS_THRESHOLDS[2] * m_acc_z < curr_acc_z <= const.BUMPS_THRESHOLDS[3] * m_acc_z:
                    bumpy_score, lon, lat = 3, gnss['lon'], gnss['lat']
                elif const.BUMPS_THRESHOLDS[3] * m_acc_z < curr_acc_z <= const.BUMPS_THRESHOLDS[4] * m_acc_z:
                    bumpy_score, lon, lat = 4, gnss['lon'], gnss['lat']
                elif curr_acc_z > const.BUMPS_THRESHOLDS[4] * m_acc_z:
                    bumpy_score, lon, lat = 5, gnss['lon'], gnss['lat']
                bumpy_scores.append({
                    "bumpy_score": bumpy_score,
                    "loc": {
                        "type": "Point",
                        "coordinates": [lon, lat]
                    }
                })
    bumps_ids = mongodb_interface.insert_new_points(bumpy_scores).inserted_ids
    return bumpy_scores, bumps_ids


def calculate_trip_statistics(trip_uuid, gnss_data):
    # distance = 0
    # prev_point = None
    # for index, curr_point in gnss_data.iterrows():
    #     if index == 0:
    #         prev_point = curr_point
    #     else:
    #         distance += haversine(prev_point['lon'], prev_point['lat'], curr_point['lon'], curr_point['lat'])
    max_speed = gnss_data['speed'].max()
    avg_speed = gnss_data['speed'].mean()
    max_elevation = gnss_data['ele'].max()
    min_elevation = gnss_data['ele'].min()
    avg_elevation = gnss_data['ele'].mean()
    mongodb_interface.update_trip_statistics(trip_uuid,  # distance,
                                             max_speed, avg_speed, max_elevation, min_elevation,
                                             avg_elevation)
    log = 'TRIP STATISTICS: max speed: %s, avg speed: %s, max ele: %s, min ele: %s, avg ele: %s\n' \
          % (max_speed, avg_speed, max_elevation, min_elevation, avg_elevation)
    return log


def run_motion_data_analysis(trip_uuid):
    # trip and motion data
    gnss_data, motion_df, db_log = retrieve_data(trip_uuid)
    if gnss_data is None or motion_df is None:
        logging.info(db_log)
        return None
    # trip statistics
    stats_log = calculate_trip_statistics(trip_uuid, gnss_data)
    # road quality
    track, motion_log = calculate_road_quality(trip_uuid, gnss_data, motion_df)
    bumps, bumps_ids = calculate_bumps(trip_uuid, gnss_data, motion_df)
    mongodb_interface.update_trip_bumpy_points(trip_uuid, bumps_ids)
    plot(motion_df, bumps)
    if const.VERBOSITY:
        logging.info(db_log + stats_log + motion_log)
    else:
        logging.info('Motion data analysis of trip %s completed' % trip_uuid)
    return track
