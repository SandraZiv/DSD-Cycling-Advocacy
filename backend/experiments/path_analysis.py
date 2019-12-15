from swagger_server import mongodb_interface as db
import pandas as pd
import os


# retrieve trip and motion file and return them as a list and a pandas dataframe respectively
def retrieve_data(trip_uuid):

    # data retrieval
    trip_data = db.get_trip_by_trip_uuid("db68af06-d350-4207-ac7b-52f6e6a37e0c")
    motion_file = db.get_file_by_filename("\"db68af06-d350-4207-ac7b-52f6e6a37e0c\"")

    # motion dataframe creation
    # need to find a way read grid file into dataframe without a temporary file
    # this is horrible!
    with open("tmp.csv", "w") as fp:
        fp.write(list(motion_file)[0].decode('utf-8'))
    with open("tmp.csv", "r") as fp:
        motion_df = pd.read_csv(fp)
    os.remove("tmp.csv")

    # trip list creation (timestamps are used as a key for points, no other data is used
    trip_ts = []
    for p in trip_data['gnss_data']:
        trip_ts.append(p['time_ts'])

    return trip_ts, motion_df


# TODO
# for each point except first one, split motion_df into chunks based on points' timestamps
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
