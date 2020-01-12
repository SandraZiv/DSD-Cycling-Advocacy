from swagger_server import constants as const, mongodb_interface
import pandas as pd
import os
import datetime
import logging
# import tkinter
import matplotlib.pyplot as plt
from math import radians, cos, sin, asin, sqrt
import random
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


def plot(trip_df, motion_df):
    """
    Plot motion data signals
    """
    plt.plot(motion_df['acc_x'], label='acc_x')
    plt.plot(motion_df['acc_y'], label='acc_y')
    plt.plot(motion_df['acc_z'], label='acc_z')
    plt.plot(motion_df['mag_x'], label='mag_x')
    plt.plot(motion_df['mag_y'], label='mag_y')
    plt.plot(motion_df['mag_z'], label='mag_z')
    plt.plot(motion_df['gyr_x'], label='gyr_x')
    plt.plot(motion_df['gyr_y'], label='gyr_y')
    plt.plot(motion_df['gyr_z'], label='gyr_z')
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
    print(motion_df)
    log = 'MOTION DATA SUMMARY DESCRIPTION\n'
    log += str(motion_df.describe())
    road_quality = []
    # time_ts is used as a primary key over gnss
    for index, ts in enumerate(gnss_data['time_ts']):
        if index == len(gnss_data['time_ts']) - 1:
            # road quality is calculated per each gnss point except the last one
            break
        # chunk is the subset of motion data registered in the time interval of current gnss
        chunk = motion_df.loc[motion_df['timestamp'] == ts]
        print(chunk)
        # calculate road quality
        # this is just a dummy example
        # timestamp,accelerometerX,accelerometerY,accelerometerZ,magnetometerX,
        # magnetometerY,magnetometerZ,gyroscopeX,gyroscopeY,gyroscopeZ
        chunk_road_quality = chunk['accelerometerZ'].mean()
        mongodb_interface.update_trip_road_quality(trip_uuid, index, chunk_road_quality)
        road_quality.append(chunk_road_quality)
    coords = [[gp['lon'], gp['lat']] for gp in mongodb_interface.get_trip_by_trip_uuid(trip_uuid)['gnss_data']]
    track = {'loc': {'type': 'LineString', 'coordinates': coords},
             'quality_scores': road_quality}
    return track, log


def calculate_trip_statistics(trip_uuid, gnss_data):
    max_speed = gnss_data['speed'].max()
    avg_speed = gnss_data['speed'].mean()
    max_elevation = gnss_data['ele'].max()
    min_elevation = gnss_data['ele'].min()
    avg_elevation = gnss_data['ele'].mean()
    mongodb_interface.update_trip_statistics(trip_uuid,
                                             max_speed, avg_speed, max_elevation, min_elevation,
                                             avg_elevation)
    log = 'TRIP STATISTICS: max speed: %s, avg speed: %s, max ele: %s, min ele: %s, avg ele: %s\n' \
          % (max_speed, avg_speed, max_elevation, min_elevation, avg_elevation)
    return log


def run_motion_data_analysis(trip_uuid):
    # trip and motion data
    gnss_data, motion_df, db_log = retrieve_data(trip_uuid)
    # trip statistics
    stats_log = calculate_trip_statistics(trip_uuid, gnss_data)
    # road quality
    track, motion_log = calculate_road_quality(trip_uuid, gnss_data, motion_df)
    if const.VERBOSITY:
        logging.info(db_log + stats_log + motion_log)
    else:
        logging.info('Motion data analysis of trip %s completed' % trip_uuid)
    return track
