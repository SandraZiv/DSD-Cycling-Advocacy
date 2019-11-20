import connexion
import six

from swagger_server.models.api_response import ApiResponse  # noqa: E501
from swagger_server.models.trip import Trip  # noqa: E501
from swagger_server import util

# TODO new trip analysis jobs can't be put in the queue until trip data and all motion files aren't fully uploaded
# when you're ready:
# import queue
# queue.new_trip_analysis_job(trip_id)


def get_trip_by_trip_uuid(trip_uuid):  # noqa: E501
    """Gets a trip given a tripUUID

     # noqa: E501

    :param trip_uuid: 
    :type trip_uuid: 

    :rtype: Trip
    """
    return 'do some magic!'


def get_trips_by_device_uuid(device_uuid):  # noqa: E501
    """Gets all the trips given a deviceUUID

     # noqa: E501

    :param device_uuid: 
    :type device_uuid: 

    :rtype: List[Trip]
    """
    return 'do some magic!'


def insert_new_trip(body):  # noqa: E501
    """Insert a new bike trip and start the background processing.

     # noqa: E501

    :param body: 
    :type body: dict | bytes

    :rtype: ApiResponse
    """
    if connexion.request.is_json:
        body = Trip.from_dict(connexion.request.get_json())  # noqa: E501
    return 'do some magic!'


def upload_motion_file():  # noqa: E501
    """Uploads a csv motion file, no checks are performed.

     # noqa: E501


    :rtype: ApiResponse
    """
    return 'do some magic!'
