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
    return main_db.trips.find_one({'tripUUID': trip_uuid})


def insert_new_trip(trip):
    main_db.trips.insert_one(trip)


#
# GRIDFS
#


fs = gridfs.GridFS(file_db)


def get_file_by_filename(filename):
    file = fs.find_one({'filename': filename})
    return fs.get(file._id)


def insert_new_file(filename, data, **kwargs):
    fs.put(data, filename=filename, **kwargs)


def delete_file_by_filename(filename, **kwargs):
    file = fs.find_one({'filename': filename})
    fs.delete(file._id, **kwargs)
