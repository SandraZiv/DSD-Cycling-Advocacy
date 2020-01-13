# coding: utf-8

from __future__ import absolute_import

from flask import json
from six import BytesIO

from swagger_server.models.api_response import ApiResponse  # noqa: E501
from swagger_server.models.bumpy_point import BumpyPoint  # noqa: E501
from swagger_server.models.track import Track  # noqa: E501
from swagger_server.test import BaseTestCase


class TestMapDataController(BaseTestCase):
    """MapDataController integration test stubs"""

    def test_get_bumpy_issue_points(self):
        """Test case for get_bumpy_issue_points

        Get the autocomputed points
        """
        query_string = [('bottomLeftLat', 0),
                        ('bottomLeftLon', 1),
                        ('topRightLat', 2),
                        ('topRightLon', 3)]
        response = self.client.open(
            '/v1/mapData/getBumpyIssuePoints',
            method='GET',
            query_string=query_string)
        self.assert200(response,
                       'Response body is : ' + response.data.decode('utf-8'))

    def test_get_road_quality_segments(self):
        """Test case for get_road_quality_segments

        Get the paths composed of segments to display the heatmap
        """
        query_string = [('bottomLeftLat', 0),
                        ('bottomLeftLon', 1),
                        ('topRightLat', 2),
                        ('topRightLon', 3)]
        response = self.client.open(
            '/v1/mapData/getRoadQualitySegments',
            method='GET',
            query_string=query_string)
        self.assert200(response,
                       'Response body is : ' + response.data.decode('utf-8'))


if __name__ == '__main__':
    import unittest
    unittest.main()
