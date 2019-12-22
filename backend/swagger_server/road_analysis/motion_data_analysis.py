from swagger_server import constants as const, mongodb_interface as db
import pandas as pd
import os
import datetime
import logging
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
    a = sin(dlat/2)**2 + cos(lat1) * cos(lat2) * sin(dlon/2)**2
    c = 2 * asin(sqrt(a))
    r = 6371 # Radius of earth in kilometers. Use 3956 for miles
    return c * r


# retrieve trip and motion file and return them as a list and a pandas dataframe respectively
def retrieve_data(trip_uuid):
    trip_data = db.get_trip_by_trip_uuid(trip_uuid)
    motion_file = db.get_file_by_filename("\"" + trip_uuid + "\"")

    log = 'MOTION DATA ANALYSIS OF TRIP %s\n' % trip_uuid
    log += 'TRIP %s\n' % trip_data

    # timestamp list creation (timestamps are used as a key for points, no other data is used
    trip_df = pd.DataFrame(trip_data['gnss_data'])

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
    motion_df = motion_df.head(-1)
    motion_df['ts'] = motion_df['ts'].apply(datetime.datetime.strptime, args=['%Y-%m-%dT%H:%M:%SZ'])
    log += 'MOTION DATA PREVIEW FOR TRIP %s %s\n' % (trip_uuid, motion_df.head(5))
    log += 'First timestamp of motion data is: %s\n' % motion_df.iloc[0]['ts']
    log += 'Last timestamp of motion data is: %s\n' % motion_df.iloc[-1]['ts']

    return trip_df, motion_df, log


# for each point into trip data, take the chunk of motion data marked with the same timestamp
# describe() shows main statistics available for each chunk
def motion_analysis(trip_df, motion_df):
    log = ''
    log += 'MOTION DATA ANALYSIS\nPRINTING OUT ONLY FIRST THREE CHUNKS OF DATA\n'
    for index, ts in enumerate(trip_df['time_ts']):
        chunk = motion_df.loc[motion_df['ts'] == ts]

        if index < 3:  # print only first 5 chunks for shortness
            log += 'CHUNK %s FOR TS %s\n' % (index, ts)
            log += '%s\n' % chunk.describe()
    max_road_quality = 0
    min_road_quality = 0
    avg_road_quality = 0
    return max_road_quality, min_road_quality, avg_road_quality, log


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
    distance, max_speed, avg_speed, max_elevation, min_elevation, avg_elevation = calculate_trip_statistics(trip_df)
    db.update_trip(trip_uuid, {'distance': distance, 'max_speed': max_speed, 'avg_speed': avg_speed,
                               'max_elevation': max_elevation, 'min_elevation': min_elevation,
                               'avg_elevation': avg_elevation})
    log += 'TRIP STATISTICS\nDistance: %s, max speed: %s, avg speed: %s, max ele: %s, min ele: %s, avg ele: %s\n' \
           % (distance, max_speed, avg_speed, max_elevation, min_elevation, avg_elevation)
    max_road_quality, min_road_quality, avg_road_quality, motion_log = motion_analysis(trip_df, motion_df)
    log += motion_log
    if const.VERBOSITY:
        logging.info(log)
    else:
        logging.info('Motion data analysis of trip %s completed\n'
                     'Statistics: distance: %s, max speed: %s, avg speed: %s, max ele: %s, min ele: %s, avg ele: %s\n'
                     'Max road quality: %s, min road quality: %s, avg road quality: %s'
                     % (trip_uuid, distance, max_speed, avg_speed, max_elevation, min_elevation, avg_elevation,
                        max_road_quality, min_road_quality, avg_road_quality))



# TODO
# for each chunk, calculate road quality
# store road quality into trips (associate road quality to each point - each points store
# the road quality btw itself and its subsequent, except for the last one)


# Store the path back into database
def store_path(trip_points):
    # todo
    pass


# Get the path from the database and return a populated list
def get_path():
    # todo
    pass


# Get the motion data from the database and return a populated list
def get_motion_data():
    # todo
    pass


def calculate_score(tmp_motion_data):
    # add up all the numbers?
    s = 0
    for dp in tmp_motion_data:
        s += dp.acx
        s += dp.acy
        s += dp.acz
    return s


# Assuming we have path points and motionData nicely stored in two arrays/lists
trip_points = []
motion_data = []

# iteration index of motion data
mi = 0

# do not want the last point of the trip
for ti, i in range(len(trip_points) - 1):
    p1 = trip_points[i]
    p2 = trip_points[i + 1]
    tmp_motion_data = []
    # get all motion data points that are before timestamp of the second gnss point
    while motion_data[mi].timeStamp < p2.timeStamp:
        tmp_motion_data.append(motion_data[mi])
        mi += 1
    p1.score = calculate_score(tmp_motion_data)

# store path back into the db
store_path(trip_points)
