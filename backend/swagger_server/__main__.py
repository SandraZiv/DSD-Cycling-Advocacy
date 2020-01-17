#!/usr/bin/env python3

import connexion

from swagger_server import encoder, constants as const
from swagger_server.road_analysis import queue
import logging


def main():

    # logger setup (application-wide)
    logging.basicConfig(filename='server_log.log', level=logging.INFO,
                        format='%(asctime)s %(message)s', datefmt='%Y-%m-%d %H:%M:%S')

    # connect to a running rabbitmq server and executes incoming jobs
    queue_listener = queue.Listener(const.TRIP_ANALYSIS_QUEUE, const.RABBITMQ_HOST)
    queue_listener.start_consuming()

    # flask based api endpoint, powered by swagger server
    app = connexion.App(__name__, specification_dir='./swagger/')
    app.app.json_encoder = encoder.JSONEncoder
    app.add_api('swagger.yaml', arguments={'title': 'Bumpy API'}, pythonic_params=True)
    app.run(port=5000, debug=True)


if __name__ == '__main__':
    main()
