#!/usr/bin/env python3

import connexion

from swagger_server import encoder, constants as const
from swagger_server.road_analysis import queue
import logging


def main():

    logging.basicConfig(filename='swagger_server/logs/default_log.log', level=logging.INFO)
    logging.info('Attempting to start a queue for road analysis...')
    try:
        queue.start_consuming(const.TRIP_ANALYSIS_QUEUE)
    except Exception:
        logging.info('Failed to instantiate a queue for road analysis')

    app = connexion.App(__name__, specification_dir='./swagger/')
    app.app.json_encoder = encoder.JSONEncoder
    app.add_api('swagger.yaml', arguments={'title': 'Bumpy API'}, pythonic_params=True)
    app.run(port=5000, debug=True)


if __name__ == '__main__':
    main()
