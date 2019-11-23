#!/usr/bin/env python3

import connexion
import logging
import time
from threading import Thread
from swagger_server.controllers import queue
from swagger_server.controllers import constants as const

from swagger_server import encoder

logging.basicConfig(filename='log.log', level=logging.INFO)


def main():
    # instantiate a listener for trip analysis queue on a new thread and tested it
    Thread(target=queue.new_listener, args=const.TRIP_ANALYSIS_QUEUE)
    time.sleep(2)
    queue.test_queue(const.TRIP_ANALYSIS_QUEUE)

    app = connexion.App(__name__, specification_dir='./swagger/')
    app.app.json_encoder = encoder.JSONEncoder
    app.add_api('swagger.yaml', arguments={'title': 'Bumpy API'}, pythonic_params=True)
    app.run(port=8080)


if __name__ == '__main__':
    main()
