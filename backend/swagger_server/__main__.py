#!/usr/bin/env python3

import connexion

from swagger_server import encoder, constants as const
from swagger_server.analysis import road_analysis


def main():

    # TODO START THE QUEUE
    # try:
    #     road_analysis.start_consuming(const.TRIP_ANALYSIS_QUEUE)
    #     print('\nRoad analysis engine is up and running!')
    #     print('Road analysis verbosity is set to: %s\n' % const.VERBOSITY)
    # except Exception:
    #     print('Could not run road analysis engine')

    app = connexion.App(__name__, specification_dir='./swagger/')
    app.app.json_encoder = encoder.JSONEncoder
    app.add_api('swagger.yaml', arguments={'title': 'Bumpy API'}, pythonic_params=True)
    app.run(port=5000, debug=True)


if __name__ == '__main__':
    main()
