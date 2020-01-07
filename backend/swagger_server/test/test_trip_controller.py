# coding: utf-8

from __future__ import absolute_import

from flask import json
from six import BytesIO

from swagger_server.models.api_response import ApiResponse  # noqa: E501
from swagger_server.models.full_processed_trip import FullProcessedTrip  # noqa: E501
from swagger_server.models.processed_trip import ProcessedTrip  # noqa: E501
from swagger_server.models.trip import Trip  # noqa: E501
from swagger_server.test import BaseTestCase


class TestTripController(BaseTestCase):
    """TripController integration test stubs"""

    def test_delete_trip(self):
        """Test case for delete_trip

        Deletes a trip.
        """
        query_string = [('tripUUID', '38400000-8cf0-11bd-b23e-10b96e4ef00d')]
        response = self.client.open(
            '/v1/trip/deleteTrip',
            method='DELETE',
            query_string=query_string)
        self.assert200(response, 'Response body is : ' + response.data.decode('utf-8'))

    def test_get_trip_by_trip_uuid(self):
        """Test case for get_trip_by_trip_uuid

        Gets a trip given a tripUUID
        """
        query_string = [('tripUUID', '38400000-8cf0-11bd-b23e-10b96e4ef00d')]
        response = self.client.open(
            '/v1/trip/getTripByTripUUID',
            method='GET',
            query_string=query_string)
        self.assert400(response, 'Response body is : ' + response.data.decode('utf-8'))

    def test_get_trips_by_device_uuid(self):
        """Test case for get_trips_by_device_uuid

        Gets all the trips given a deviceUUID
        """
        query_string = [('deviceUUID', '38400000-8cf0-11bd-b23e-10b96e4ef00d')]
        response = self.client.open(
            '/v1/trip/getTripsByDeviceUUID',
            method='GET',
            query_string=query_string)
        self.assert200(response, 'Response body is : ' + response.data.decode('utf-8'))

    def test_insert_new_trip(self):
        """Test case for insert_new_trip

        Insert a new bike trip and start the background processing.
        """
        body = Trip()
        response = self.client.open(
            '/v1/trip/insertNewTrip',
            method='POST',
            data=json.dumps(body),
            content_type='application/json')
        self.assert200(response, 'Response body is : ' + response.data.decode('utf-8'))

    def test_upload_motion_file(self):
        """Test case for upload_motion_file

        Uploads a csv motion file, only a few checks are performed.
        """
        data = dict(file='file_example',
                    tripUUID='38400000-8cf0-11bd-b23e-10b96e4ef00d')
        response = self.client.open(
            '/v1/trip/uploadMotionFile',
            method='POST',
            data=data,
            content_type='multipart/form-data')
        self.assert400(response, 'Response body is : ' + response.data.decode('utf-8'))


if __name__ == '__main__':
    import unittest
    unittest.main()
