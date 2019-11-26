#!/usr/bin/env python3

import connexion
from swagger_server.controllers import queue
from swagger_server.controllers import constants as const

import time
from swagger_server import encoder


def main():
    # start threads with queue listener
    # TODO handle reconnection
    queue.new_listener(const.TRIP_ANALYSIS_QUEUE)
    queue.Job(
        job_type=const.TRIP_ANALYSIS_JOB,
        job_data='DUMMY TRIP FOR TRIP ANALYSIS').enqueue_job(const.TRIP_ANALYSIS_QUEUE)
    queue.new_listener(const.MAP_UPDATE_QUEUE)
    queue.Job(
        job_type=const.MAP_UPDATE_JOB,
        job_data='DUMMY TRIP FOR MAP UPDATE').enqueue_job(const.MAP_UPDATE_QUEUE)

    app = connexion.App(__name__, specification_dir='./swagger/')
    app.app.json_encoder = encoder.JSONEncoder
    app.add_api('swagger.yaml', arguments={'title': 'Bumpy API'}, pythonic_params=True)
    app.run(port=8080)


if __name__ == '__main__':
    main()
