import connexion
import six

from swagger_server.models.api_response import ApiResponse  # noqa: E501
from swagger_server.models.full_processed_trip import FullProcessedTrip  # noqa: E501
from swagger_server.models.processed_trip import ProcessedTrip  # noqa: E501
from swagger_server.models.trip import Trip  # noqa: E501
from swagger_server.models.bumpy_point import BumpyPoint  # noqa: E501
from swagger_server.road_analysis import queue
from swagger_server import constants as const
from swagger_server import mongodb_interface
from flask import make_response

# TODO new trip road_analysis jobs can't be put in the queue until trip data and all motion files aren't fully uploaded
# when you're ready:
# import queue
# queue.new_trip_analysis_job(trip_id)


def delete_trip():  # noqa: E501
    """Deletes a trip.

     # noqa: E501

    :param trip_uuid:
    :type trip_uuid:

    :rtype: ApiResponse
    """
    trip_uuid = connexion.request.args.get('tripUUID', None)
    mongodb_interface.delete_trip_by_trip_uuid(trip_uuid)
    return ApiResponse(code=200, message="ok"), 200


def get_motion_file():  # noqa: E501
    """Gets a csv motion file.

     # noqa: E501

    :param trip_uuid:
    :type trip_uuid:

    :rtype: str
    """
    trip_uuid = connexion.request.args.get('tripUUID', None)
    file = mongodb_interface.get_file_by_filename(trip_uuid)
    if not file:
        return ApiResponse(code=400, message='trip not found'), 400
    output = make_response(file.read())
    output.headers['Content-Disposition'] = 'attachment; filename={}.csv'.format(trip_uuid)
    output.headers['Content-type'] = 'text/csv'
    return output


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
    raw_points = mongodb_interface.get_points_by_points_id(trip.get('bumpy_points', []))
    output_points = []
    for raw_point in raw_points:
        output_points.append({
            'lat': raw_point['loc']['coordinates'][1],
            'lon': raw_point['loc']['coordinates'][0],
            'bump_score': raw_point['bump_score']
        })
    trip['bumpy_points'] = list(map(BumpyPoint.from_dict, output_points))
    return FullProcessedTrip().from_dict(trip), 200


def get_trips_by_device_uuid():  # noqa: E501
    """Gets all the trips given a deviceUUID

     # noqa: E501

    :param device_uuid: 
    :type device_uuid: 

    :rtype: List[Trip]
    """
    device_uuid = connexion.request.args.get('deviceUUID', None)
    trips = mongodb_interface.get_trips_by_device_uuid(device_uuid)
    for trip in trips:
        trip['bumpy_points_count'] = len(trip.get('bumpy_points', []))
    return list(map(ProcessedTrip.from_dict, trips)), 200


def insert_new_trip():  # noqa: E501
    """Insert a new bike trip and start the background processing.

     # noqa: E501

    :param body: 
    :type body: dict | bytes

    :rtype: ApiResponse
    """
    if not connexion.request.is_json:
        return ApiResponse(code=400, message="not a valid json"), 400
    try:
        body = Trip.from_dict(connexion.request.get_json())  # noqa: E501
        mongodb_interface.insert_new_trip(body.to_dict())
    except mongodb_interface.pymongo.errors.DuplicateKeyError:
        return ApiResponse(code=400, message="trip already exist"), 400
    return ApiResponse(code=200, message="ok"), 200


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
        mongodb_interface.insert_new_file(trip_uuid.replace("\"", ""), file)
        # enqueueing a trip road_analysis job
        queue.Job(job_type=const.TRIP_ANALYSIS_JOB, job_data=trip_uuid.replace("\"", "")).enqueue_job(
            const.TRIP_ANALYSIS_QUEUE, const.RABBITMQ_HOST)
        return ApiResponse(code=200, message='ok'), 200
    return ApiResponse(code=400, message='wrong file selected'), 400
