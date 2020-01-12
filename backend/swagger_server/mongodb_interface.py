from flask import Flask
from bson.objectid import ObjectId
import gridfs
import pymongo

mongodbUri = 'mongodb+srv://app:kk1AVXNUwsAoqnyx@bumpy-tdkiu.gcp.mongodb.net/test?retryWrites=true&w=majority'

app = Flask(__name__)
client = pymongo.MongoClient(mongodbUri, maxPoolSize=10, connect=False)
main_db = client.main_db
file_db = client.file_db


#
# TRIPS
#


def get_trip_by_trip_uuid(trip_uuid):
    return main_db.trips.find_one({'trip_uuid': trip_uuid})


def insert_new_trip(trip):
    main_db.trips.insert_one(trip)


def get_trips_by_device_uuid(device_uuid):
    return main_db.trips.find({'device_uuid': device_uuid}).sort([('end_ts', pymongo.DESCENDING)])


def update_trip_statistics(trip_uuid, distance, max_speed, avg_speed, max_elevation, min_elevation, avg_elevation):
    new_values = {
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
    }
    main_db.trips.update_one({'trip_uuid': trip_uuid}, {"$set": new_values}, upsert=False)
    return


# TODO update road quality https://docs.mongodb.com/manual/tutorial/update-documents/
def update_trip_road_quality(trip_uuid, gnss_index, road_quality):
    main_db.trips.update_one({'trip_uuid': trip_uuid},
                             {"$set": {'gnss_data.' + str(gnss_index) + '.road_quality': road_quality}})


def delete_trip_by_trip_uuid(trip_uuid):
    main_db.trips.delete_one({'trip_uuid': trip_uuid})


#
# DEVICES UUID
#


def get_device_uuid_map_by_short_device_uuid(short_device_uuid):
    return main_db.devices_uuid_map.find_one({'short_device_uuid': short_device_uuid})


def get_device_uuid_map_by_device_uuid(device_uuid):
    return main_db.devices_uuid_map.find_one({'device_uuid': device_uuid})


def insert_new_device_uuid_map(device_uuid_map):
    main_db.devices_uuid_map.insert_one(device_uuid_map)


#
# TRACKS
#


def get_tracks_by_intersect_geometry(geometry):
    return main_db.tracks.find({'loc': {'$geoIntersects': geometry}})


def insert_new_tracks(tracks):
    main_db.tracks.insert_many(tracks)


def delete_tracks(tracks_id):
    main_db.tracks.delete_many({'_id': {'$in': list(map(ObjectId, tracks_id))}})


#
# POINTS
#


def get_points_by_distance_geometry(geometry):
    return main_db.points.find({'loc': {'$near': geometry}, '$maxDistance': 10})


def get_points_by_points_id(points_id):
    return main_db.points.find({'_id': {'$in': list(map(ObjectId, points_id))}})


def get_points_by_intersect_geometry(geometry):
    return main_db.points.find({'loc': {'$geoIntersects': geometry}})


def insert_new_points(points):
    main_db.points.insert_many(points)


#
# GRIDFS
#


fs = gridfs.GridFS(file_db)


def get_file_by_filename(filename):
    file = fs.find_one({'filename': filename})
    if file:
        return fs.get(file._id)


def insert_new_file(filename, data, **kwargs):
    # strip removes double-double quotes from filename. should be handled in the frontend, but doesn't matter
    fs.put(data, filename=filename.replace("\"", ""), **kwargs)


def delete_file_by_filename(filename, **kwargs):
    file = fs.find_one({'filename': filename})
    fs.delete(file._id, **kwargs)
