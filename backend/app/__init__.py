# -*- coding: utf-8 -*-
from flask import Flask, request, jsonify
from flask.logging import default_handler
from app import settings
import logging
import pymongo
import json
import os

app = Flask(__name__)
#app.register_blueprint(notifications, url_prefix='/api/notifications')


# configuring logging
logging.basicConfig(handlers=[default_handler], level=logging.INFO)
app.logger.info('Done configuring logging')


# loading json schemas
app.logger.info('Starting loading json schemas')
schema_path = os.path.join(os.path.dirname(os.path.realpath(__file__)), settings.config.get('json_validate', 'schema_folder'))
files = [f for f in os.listdir(schema_path) if f.endswith('.schema')]
for file in files:
    with open(os.path.join(schema_path, file)) as f:
        app.config[file[:-len('.schema')]] = json.load(f)
app.logger.info('Done loading {} json schemas'.format(len(files)))


@app.route('/', methods=['POST', 'GET'])
## 	The index function logs 'Hello World'.
# 	It doesn't need any parameter.
def index():
    app.logger.info('Hello world, the API is working!')
    return 'Hello World, the API is working!'


@app.errorhandler(404)
## 	The not_found function handles the case of a HTTP 404 response from the client.
#	@param error The error generated. Note that only HTTP 404 errors can be handled by this function
def not_found(error):
    app.logger.error(error)
    return jsonify({'success': False, 'error': 'resource not found', 'code': 404}), 404


@app.errorhandler(pymongo.errors.DuplicateKeyError)
def pymongo_duplicate_key_error_handler(error):
    app.logger.error(error)
    return jsonify({'success': False, 'error': 'mongodb duplicate key detected', 'code': 409}), 409


@app.errorhandler(pymongo.errors.AutoReconnect)
def pymongo_autoreconnect_error_handler(error):
    app.logger.error(error)
    return jsonify({'success': False, 'error': 'mongodb connection error', 'code': 503}), 503


@app.after_request
##	The after function is called after a request has been sent from the server to the client.
#	Its main purpose is to log  the info of the request if it gets the HTTP error code 400
def after(response):
    if not 200 <= response.status_code < 300:
        app.logger.info(request.get_data())
        app.logger.info(response.get_data())
    return response


if __name__ == '__main__':
    app.run(debug=True)
