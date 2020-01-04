from swagger_server import constants as const, mongodb_interface as db
import pandas as pd
import os
import datetime
import logging
# import tkinter
import matplotlib.pyplot as plt
from math import radians, cos, sin, asin, sqrt


def haversine(lon1, lat1, lon2, lat2):
    """
    Calculate the great circle distance between two points
    on the earth (specified in decimal degrees)
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


# retrieve trip and motion file and return them as a list and a pandas dataframe respectively
def retrieve_data(trip_uuid):
    trip_data = db.get_trip_by_trip_uuid(trip_uuid)
    motion_file = db.get_file_by_filename("\""+trip_uuid+"\"")
    # motion_file = db.get_file_by_filename(trip_uuid)
    trip_df = pd.DataFrame(trip_data['gnss_data'])
    log = 'MOTION DATA ANALYSIS OF TRIP %s\n' % trip_uuid
    log += 'First timestamp of trip is: %s\n' % trip_df.head(1)['time_ts']
    log += 'Last timestamp of trip is: %s\n' % trip_df.tail(1)['time_ts']
    # motion dataframe creation
    # TODO find a way to read grid file into dataframe without a temporary file
    # this is horrible!
    with open("tmp.csv", "w") as fp:
        fp.write("ts,acc_x,acc_y,acc_z,mag_x,mag_y,mag_z,gyr_x,gyr_y,gyr_z\n")
        fp.write(list(motion_file)[0].decode('utf-8'))
    with open("tmp.csv", "r") as fp:
        motion_df = pd.read_csv(fp)
    os.remove("tmp.csv")
    # TODO DEBUG ONLY there's an error in last row of the motion file I'm using, so I remove it
    # motion_df = motion_df.head(-1)
    motion_df['ts'] = motion_df['ts'].apply(datetime.datetime.strptime, args=['%Y-%m-%dT%H:%M:%SZ'])
    log += 'First timestamp of motion data is: %s\n' % motion_df.iloc[0]['ts']
    log += 'Last timestamp of motion data is: %s\n' % motion_df.iloc[-1]['ts']

    return trip_df, motion_df, log


# for each point into trip data, take the chunk of motion data marked with the same timestamp
# describe() shows main statistics available for each chunk
def calculate_road_quality(trip_uuid, trip_df, motion_df):
    log = 'MOTION DATA SUMMARY DESCRIPTION\n'
    log += str(motion_df.describe())
    for index, ts in enumerate(trip_df['time_ts']):
        chunk = motion_df.loc[motion_df['ts'] == ts]
        # calculate road quality
        # this is just a dummy example
        road_quality = chunk['acc_z'].mean()
        db.update_trip_road_quality(trip_uuid, index, road_quality)
    max_road_quality = 0
    min_road_quality = 0
    avg_road_quality = 0
    return max_road_quality, min_road_quality, avg_road_quality, log


def plot(trip_df, motion_df):
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


def calculate_trip_statistics(trip_df):
    distance = haversine(trip_df.head(1)['lon'], trip_df.head(1)['lat'],
                         trip_df.tail(1)['lon'], trip_df.tail(1)['lat'])
    max_speed = trip_df['speed'].max()
    avg_speed = trip_df['speed'].mean()
    max_elevation = trip_df['ele'].max()
    min_elevation = trip_df['ele'].min()
    avg_elevation = trip_df['ele'].mean()
    return distance, max_speed, avg_speed, max_elevation, min_elevation, avg_elevation


def run_motion_data_analysis(trip_uuid):
    trip_df, motion_df, log = retrieve_data(trip_uuid)
    plot(trip_df, motion_df)
    distance, max_speed, avg_speed, max_elevation, min_elevation, avg_elevation = calculate_trip_statistics(trip_df)
    db.update_trip_statistics(trip_uuid,
                              {
                                  "elevation": {
                                      "minElevation": min_elevation,
                                      "maxElevation": max_elevation,
                                      "avgElevation": avg_elevation
                                  },
                                  "distance": distance,
                                  "speed": {
                                      "maxSpeed": max_speed,
                                      "avgSpeed": avg_speed
                                  }
                              })
    log += 'TRIP STATISTICS: distance: %s, max speed: %s, avg speed: %s, max ele: %s, min ele: %s, avg ele: %s\n' \
           % (distance, max_speed, avg_speed, max_elevation, min_elevation, avg_elevation)
    max_road_quality, min_road_quality, avg_road_quality, motion_log = \
        calculate_road_quality(trip_uuid, trip_df, motion_df)
    log += motion_log
    if const.VERBOSITY:
        logging.info(log)
    else:
        logging.info('Motion data analysis of trip %s completed\n'
                     'Statistics: distance: %s, max speed: %s, avg speed: %s, max ele: %s, min ele: %s, avg ele: %s\n'
                     'Max road quality: %s, min road quality: %s, avg road quality: %s'
                     % (trip_uuid, distance, max_speed, avg_speed, max_elevation, min_elevation, avg_elevation,
                        max_road_quality, min_road_quality, avg_road_quality))
