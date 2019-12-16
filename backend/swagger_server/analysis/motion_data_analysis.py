from swagger_server import constants as const, mongodb_interface as db
import pandas as pd
import os
import datetime


# retrieve trip and motion file and return them as a list and a pandas dataframe respectively
def retrieve_data(trip_uuid):
    # data retrieval
    trip_data = db.get_trip_by_trip_uuid(trip_uuid)
    motion_file = db.get_file_by_filename("\"" + trip_uuid + "\"")
    # timestamp list creation (timestamps are used as a key for points, no other data is used
    trip_ts = []
    for p in trip_data['gnss_data']:
        trip_ts.append(p['time_ts'])
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

    if const.VERBOSITY:
        print('\nMOTION DATA ANALYSIS OF TRIP [ %s ]\n' % trip_uuid)
        print('First timestamp of motion data is: %s' % motion_df.iloc[0]['ts'])
        print('Last timestamp of motion data is: %s' % motion_df.iloc[-1]['ts'])
        print('First timestamp of trip is: %s' % trip_ts[0])
        print('Last timestamp of trip is: %s' % trip_ts[-1])

    return trip_ts, motion_df


# for each point into trip data, take the chunk of motion data marked with the same timestamp
# describe() shows main statistics available for each chunk
def motion_analysis(trip_ts, motion_df):
    if const.VERBOSITY:
        print('\nMOTION DATA ANALYSIS\n')
    for index, ts in enumerate(trip_ts):
        chunk = motion_df.loc[motion_df['ts'] == ts]
        if index < 5:  # print only first 5 chunks for shortness
            if const.VERBOSITY:
                print('CHUNK FOR TS %s' % ts)
                print(chunk.describe())
    return


def run_motion_data_analysis(trip_uuid):
    trip_ts, motion_df = retrieve_data(trip_uuid)
    motion_analysis(trip_ts, motion_df)


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
