from flask import Flask
from bson.objectid import ObjectId
import gridfs
import pymongo
import settings

app = Flask(__name__)
client = pymongo.MongoClient(settings.config.get('mongodb', 'uri'), maxPoolSize=10, connect=False)
main_db = client.main_db
file_db = client.file_db

#
# USERS
#


def insert_new_user(user_data, **kwargs):
    return main_db.users.update_one({'username': user_data['username']}, {'$setOnInsert': user_data}, upsert=True, **kwargs)


def get_user_by_user_id(user_id):
    return main_db.users.find_one({'_id': ObjectId(user_id)})


def get_users_by_user_id(users_id):
    return main_db.users.find({'_id': {'$in': users_id}})


def get_user_by_username(username):
    return main_db.users.find_one({'username': username})


#
# GRIDFS
#


fs = gridfs.GridFS(file_db)


def get_file_by_filename(filename):
    file = fs.find_one({'filename': filename})
    if file:
        return fs.get(file._id)


def insert_new_file(filename, data, **kwargs):
    file = fs.find_one({'filename': filename})
    if not file:
        fs.put(data, filename=filename, **kwargs)


def delete_file_by_filename(filename, **kwargs):
    file = fs.find_one({'filename': filename})
    if file:
        fs.delete(file._id, **kwargs)
