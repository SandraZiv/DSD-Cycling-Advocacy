import connexion
import six

from swagger_server.models.track import Track  # noqa: E501
from swagger_server import mongodb_interface


def get_bumpy_issue_points():  # noqa: E501
    """Get the autocomputed points

     # noqa: E501

    :param bottom_left_lat: The bottom left coordinate of the corner of the screen
    :type bottom_left_lat: float
    :param bottom_left_lon: The bottom left coordinate of the corner of the screen
    :type bottom_left_lon: float
    :param top_right_lat: The top right coordinate of the corner of the screen
    :type top_right_lat: float
    :param top_right_lon: The top right coordinate of the corner of the screen
    :type top_right_lon: float

    :rtype: List[BumpyPoint]
    """
    return 'do some magic!'


def get_fix_my_street_issue_points():  # noqa: E501
    """Get the FixMyStreet points

     # noqa: E501

    :param bottom_left_lat: The bottom left coordinate of the corner of the screen
    :type bottom_left_lat: float
    :param bottom_left_lon: The bottom left coordinate of the corner of the screen
    :type bottom_left_lon: float
    :param top_right_lat: The top right coordinate of the corner of the screen
    :type top_right_lat: float
    :param top_right_lon: The top right coordinate of the corner of the screen
    :type top_right_lon: float

    :rtype: List[FixMyStreetPoint]
    """
    return 'do some magic!'


def get_road_quality_segments():  # noqa: E501
    """Get the paths composed of segments to display the heatmap

     # noqa: E501

    :param bottom_left_lat: The bottom left coordinate of the corner of the screen
    :type bottom_left_lat: float
    :param bottom_left_lon: The bottom left coordinate of the corner of the screen
    :type bottom_left_lon: float
    :param top_right_lat: The top right coordinate of the corner of the screen
    :type top_right_lat: float
    :param top_right_lon: The top right coordinate of the corner of the screen
    :type top_right_lon: float

    :rtype: List[Path]
    """
    bottom_left_lat = float(connexion.request.args.get('bottomLeftLat', '0.0'))
    bottom_left_lon = float(connexion.request.args.get('bottomLeftLon', '0.0'))
    top_right_lat = float(connexion.request.args.get('topRightLat', '0.0'))
    top_right_lon = float(connexion.request.args.get('topRightLon', '0.0'))
    geometry = {'$geometry': {'type': 'Polygon', 'coordinates': [[
        [bottom_left_lon, bottom_left_lat],
        [bottom_left_lon, top_right_lat],
        [top_right_lon, top_right_lat],
        [top_right_lon, bottom_left_lat],
        [bottom_left_lon, bottom_left_lat]
    ]]}}
    raw_tracks = mongodb_interface.get_tracks_by_intersect_geometry(geometry)
    output_tracks = []
    for raw_track in raw_tracks:
        segments_coords = zip(raw_track['loc']['coordinates'][:-1], raw_track['loc']['coordinates'][1:])
        quality_scores = iter(raw_track['quality_scores'])
        output_tracks.append({'segments': [
            {'start_lat': sc[0][1],
             'start_lon': sc[0][0],
             'end_lat': sc[1][1],
             'end_lon': sc[1][0],
             'quality_score': next(quality_scores)
             } for sc in segments_coords]})
    return list(map(Track.from_dict, output_tracks)), 200
