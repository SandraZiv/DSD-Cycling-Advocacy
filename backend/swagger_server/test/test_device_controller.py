# coding: utf-8

from __future__ import absolute_import

from flask import json
from six import BytesIO

from swagger_server.models.api_response import ApiResponse  # noqa: E501
from swagger_server.test import BaseTestCase


class TestDeviceController(BaseTestCase):
    """DeviceController integration test stubs"""

    def test_get_long_device_uuid(self):
        """Test case for get_long_device_uuid

        Get the long device UUID from the short device UUID
        """
        query_string = [('short_device_uuid', 'short_device_uuid_example')]
        response = self.client.open(
            '/v1/device/getLongDeviceUUID',
            method='GET',
            query_string=query_string)
        self.assert200(response,
                       'Response body is : ' + response.data.decode('utf-8'))

    def test_get_short_device_uuid(self):
        """Test case for get_short_device_uuid

        Get the short device UUID from the long device UUID
        """
        query_string = [('device_uuid', '38400000-8cf0-11bd-b23e-10b96e4ef00d')]
        response = self.client.open(
            '/v1/device/getShortDeviceUUID',
            method='GET',
            query_string=query_string)
        self.assert200(response,
                       'Response body is : ' + response.data.decode('utf-8'))


if __name__ == '__main__':
    import unittest
    unittest.main()
