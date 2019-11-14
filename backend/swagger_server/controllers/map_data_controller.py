import connexion
import six

from swagger_server.models.api_response import ApiResponse  # noqa: E501
from swagger_server.models.bumpy_point import BumpyPoint  # noqa: E501
from swagger_server.models.fix_my_street_point import FixMyStreetPoint  # noqa: E501
from swagger_server.models.path import Path  # noqa: E501
from swagger_server import util


def get_bumpy_issue_points(bottom_left_lat, bottom_left_lon, top_right_lat, top_right_lon):  # noqa: E501
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


def get_fix_my_street_issue_points(bottom_left_lat, bottom_left_lon, top_right_lat, top_right_lon):  # noqa: E501
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


def get_road_quality_segments(bottom_left_lat, bottom_left_lon, top_right_lat, top_right_lon):  # noqa: E501
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
    return 'do some magic!'
