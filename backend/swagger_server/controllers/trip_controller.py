import connexion
import six

from swagger_server.models.api_response import ApiResponse  # noqa: E501
from swagger_server.models.trip import Trip  # noqa: E501
from swagger_server import util
from swagger_server import mongodb_interface
from bson.json_util import dumps


# TODO new trip analysis jobs can't be put in the queue until trip data and all motion files aren't fully uploaded
# when you're ready:
# import queue
# queue.new_trip_analysis_job(trip_id)


def get_trip_by_trip_uuid():  # noqa: E501
    """Gets a trip given a tripUUID

     # noqa: E501

    :param trip_uuid: 
    :type trip_uuid: 

    :rtype: Trip
    """
    trip_uuid = connexion.request.args.get('tripUUID', None)
    trip = mongodb_interface.get_trip_by_trip_uuid(trip_uuid)
    if not trip:
        return ApiResponse(code=400, message='trip not found'), 400
    return dumps(trip), 200


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
    """Uploads a csv motion file, only a few checks are performed.

     # noqa: E501

    :param file:
    :type file: strstr
    :param trip_uuid:
    :type trip_uuid:

    :rtype: ApiResponse
    """
    # if user does not select file, browser also
    # submit an empty part without filename
    file = connexion.request.files['file']
    trip_uuid = connexion.request.form['tripUUID']
    if file.filename == '':
        return ApiResponse(code=400, message='no file selected'), 400
    if file and '.' in file.filename and file.filename.rsplit('.', 1)[1].lower() == 'csv':
        mongodb_interface.insert_new_file(trip_uuid, file)
        return ApiResponse(code=200, message='ok'), 200
    return ApiResponse(code=400, message='wrong file selected'), 400
