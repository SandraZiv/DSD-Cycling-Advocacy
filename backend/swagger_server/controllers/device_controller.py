import connexion
import six

from swagger_server.models.api_response import ApiResponse  # noqa: E501
from swagger_server import mongodb_interface
import random
import string


def get_long_device_uuid():  # noqa: E501
    """Get the long device UUID from the short device UUID

     # noqa: E501

    :param short_device_uuid: 
    :type short_device_uuid: str

    :rtype: str
    """
    short_device_uuid = connexion.request.args.get('shortDeviceUUID', None)
    device_uuid_map = mongodb_interface.get_device_uuid_map_by_short_device_uuid(short_device_uuid)
    if not device_uuid_map:
        return ApiResponse(code=404, message='device uuid not found'), 404
    return device_uuid_map['device_uuid']


def get_short_device_uuid():  # noqa: E501
    """Get the short device UUID from the long device UUID

     # noqa: E501

    :param device_uuid: 
    :type device_uuid: 

    :rtype: str
    """
    device_uuid = connexion.request.args.get('deviceUUID', None)
    device_uuid_map = mongodb_interface.get_device_uuid_map_by_device_uuid(device_uuid)
    if not device_uuid_map:
        for i in range(50):
            try:
                rand = ''.join(random.choices(string.ascii_uppercase + string.digits, k=6))
                mongodb_interface.insert_new_device_uuid_map({'device_uuid': device_uuid, 'short_device_uuid': rand})
                return rand
            except mongodb_interface.pymongo.errors.DuplicateKeyError:
                pass
        raise Exception('Cannot find a unique short device uuid')
    return device_uuid_map['short_device_uuid']
