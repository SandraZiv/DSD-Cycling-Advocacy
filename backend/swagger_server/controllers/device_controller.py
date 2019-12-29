import connexion
import six

from swagger_server.models.api_response import ApiResponse  # noqa: E501
from swagger_server import util


def get_long_device_uuid(short_device_uuid):  # noqa: E501
    """Get the long device UUID from the short device UUID

     # noqa: E501

    :param short_device_uuid: 
    :type short_device_uuid: str

    :rtype: str
    """
    return 'do some magic!'


def get_short_device_uuid(device_uuid):  # noqa: E501
    """Get the short device UUID from the long device UUID

     # noqa: E501

    :param device_uuid: 
    :type device_uuid: 

    :rtype: str
    """
    return 'do some magic!'
