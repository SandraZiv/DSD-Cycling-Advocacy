# coding: utf-8

from __future__ import absolute_import
from datetime import date, datetime  # noqa: F401

from typing import List, Dict  # noqa: F401

from swagger_server.models.base_model_ import Model
from swagger_server.models.bumpy_point import BumpyPoint  # noqa: F401,E501
from swagger_server.models.processed_trip_elevation import ProcessedTripElevation  # noqa: F401,E501
from swagger_server.models.processed_trip_speed import ProcessedTripSpeed  # noqa: F401,E501
from swagger_server.models.processed_trip_vibration import ProcessedTripVibration  # noqa: F401,E501
from swagger_server.models.trip_gnss_data import TripGnssData  # noqa: F401,E501
from swagger_server import util


class FullProcessedTrip(Model):
    """NOTE: This class is auto generated by the swagger code generator program.

    Do not edit the class manually.
    """
    def __init__(self, device_uuid: str=None, trip_uuid: str=None, start_ts: datetime=None, end_ts: datetime=None, distance: float=None, speed: ProcessedTripSpeed=None, elevation: ProcessedTripElevation=None, vibration: ProcessedTripVibration=None, bumpy_points: List[BumpyPoint]=None, gnss_data: List[TripGnssData]=None):  # noqa: E501
        """FullProcessedTrip - a model defined in Swagger

        :param device_uuid: The device_uuid of this FullProcessedTrip.  # noqa: E501
        :type device_uuid: str
        :param trip_uuid: The trip_uuid of this FullProcessedTrip.  # noqa: E501
        :type trip_uuid: str
        :param start_ts: The start_ts of this FullProcessedTrip.  # noqa: E501
        :type start_ts: datetime
        :param end_ts: The end_ts of this FullProcessedTrip.  # noqa: E501
        :type end_ts: datetime
        :param distance: The distance of this FullProcessedTrip.  # noqa: E501
        :type distance: float
        :param speed: The speed of this FullProcessedTrip.  # noqa: E501
        :type speed: ProcessedTripSpeed
        :param elevation: The elevation of this FullProcessedTrip.  # noqa: E501
        :type elevation: ProcessedTripElevation
        :param vibration: The vibration of this FullProcessedTrip.  # noqa: E501
        :type vibration: ProcessedTripVibration
        :param bumpy_points: The bumpy_points of this FullProcessedTrip.  # noqa: E501
        :type bumpy_points: List[BumpyPoint]
        :param gnss_data: The gnss_data of this FullProcessedTrip.  # noqa: E501
        :type gnss_data: List[TripGnssData]
        """
        self.swagger_types = {
            'device_uuid': str,
            'trip_uuid': str,
            'start_ts': datetime,
            'end_ts': datetime,
            'distance': float,
            'speed': ProcessedTripSpeed,
            'elevation': ProcessedTripElevation,
            'vibration': ProcessedTripVibration,
            'bumpy_points': List[BumpyPoint],
            'gnss_data': List[TripGnssData]
        }

        self.attribute_map = {
            'device_uuid': 'deviceUUID',
            'trip_uuid': 'tripUUID',
            'start_ts': 'startTS',
            'end_ts': 'endTS',
            'distance': 'distance',
            'speed': 'speed',
            'elevation': 'elevation',
            'vibration': 'vibration',
            'bumpy_points': 'bumpyPoints',
            'gnss_data': 'gnssData'
        }
        self._device_uuid = device_uuid
        self._trip_uuid = trip_uuid
        self._start_ts = start_ts
        self._end_ts = end_ts
        self._distance = distance
        self._speed = speed
        self._elevation = elevation
        self._vibration = vibration
        self._bumpy_points = bumpy_points
        self._gnss_data = gnss_data

    @classmethod
    def from_dict(cls, dikt) -> 'FullProcessedTrip':
        """Returns the dict as a model

        :param dikt: A dict.
        :type: dict
        :return: The FullProcessedTrip of this FullProcessedTrip.  # noqa: E501
        :rtype: FullProcessedTrip
        """
        return util.deserialize_model(dikt, cls)

    @property
    def device_uuid(self) -> str:
        """Gets the device_uuid of this FullProcessedTrip.


        :return: The device_uuid of this FullProcessedTrip.
        :rtype: str
        """
        return self._device_uuid

    @device_uuid.setter
    def device_uuid(self, device_uuid: str):
        """Sets the device_uuid of this FullProcessedTrip.


        :param device_uuid: The device_uuid of this FullProcessedTrip.
        :type device_uuid: str
        """

        self._device_uuid = device_uuid

    @property
    def trip_uuid(self) -> str:
        """Gets the trip_uuid of this FullProcessedTrip.


        :return: The trip_uuid of this FullProcessedTrip.
        :rtype: str
        """
        return self._trip_uuid

    @trip_uuid.setter
    def trip_uuid(self, trip_uuid: str):
        """Sets the trip_uuid of this FullProcessedTrip.


        :param trip_uuid: The trip_uuid of this FullProcessedTrip.
        :type trip_uuid: str
        """

        self._trip_uuid = trip_uuid

    @property
    def start_ts(self) -> datetime:
        """Gets the start_ts of this FullProcessedTrip.


        :return: The start_ts of this FullProcessedTrip.
        :rtype: datetime
        """
        return self._start_ts

    @start_ts.setter
    def start_ts(self, start_ts: datetime):
        """Sets the start_ts of this FullProcessedTrip.


        :param start_ts: The start_ts of this FullProcessedTrip.
        :type start_ts: datetime
        """

        self._start_ts = start_ts

    @property
    def end_ts(self) -> datetime:
        """Gets the end_ts of this FullProcessedTrip.


        :return: The end_ts of this FullProcessedTrip.
        :rtype: datetime
        """
        return self._end_ts

    @end_ts.setter
    def end_ts(self, end_ts: datetime):
        """Sets the end_ts of this FullProcessedTrip.


        :param end_ts: The end_ts of this FullProcessedTrip.
        :type end_ts: datetime
        """

        self._end_ts = end_ts

    @property
    def distance(self) -> float:
        """Gets the distance of this FullProcessedTrip.

        Expressed in meters  # noqa: E501

        :return: The distance of this FullProcessedTrip.
        :rtype: float
        """
        return self._distance

    @distance.setter
    def distance(self, distance: float):
        """Sets the distance of this FullProcessedTrip.

        Expressed in meters  # noqa: E501

        :param distance: The distance of this FullProcessedTrip.
        :type distance: float
        """

        self._distance = distance

    @property
    def speed(self) -> ProcessedTripSpeed:
        """Gets the speed of this FullProcessedTrip.


        :return: The speed of this FullProcessedTrip.
        :rtype: ProcessedTripSpeed
        """
        return self._speed

    @speed.setter
    def speed(self, speed: ProcessedTripSpeed):
        """Sets the speed of this FullProcessedTrip.


        :param speed: The speed of this FullProcessedTrip.
        :type speed: ProcessedTripSpeed
        """

        self._speed = speed

    @property
    def elevation(self) -> ProcessedTripElevation:
        """Gets the elevation of this FullProcessedTrip.


        :return: The elevation of this FullProcessedTrip.
        :rtype: ProcessedTripElevation
        """
        return self._elevation

    @elevation.setter
    def elevation(self, elevation: ProcessedTripElevation):
        """Sets the elevation of this FullProcessedTrip.


        :param elevation: The elevation of this FullProcessedTrip.
        :type elevation: ProcessedTripElevation
        """

        self._elevation = elevation

    @property
    def vibration(self) -> ProcessedTripVibration:
        """Gets the vibration of this FullProcessedTrip.


        :return: The vibration of this FullProcessedTrip.
        :rtype: ProcessedTripVibration
        """
        return self._vibration

    @vibration.setter
    def vibration(self, vibration: ProcessedTripVibration):
        """Sets the vibration of this FullProcessedTrip.


        :param vibration: The vibration of this FullProcessedTrip.
        :type vibration: ProcessedTripVibration
        """

        self._vibration = vibration

    @property
    def bumpy_points(self) -> List[BumpyPoint]:
        """Gets the bumpy_points of this FullProcessedTrip.


        :return: The bumpy_points of this FullProcessedTrip.
        :rtype: List[BumpyPoint]
        """
        return self._bumpy_points

    @bumpy_points.setter
    def bumpy_points(self, bumpy_points: List[BumpyPoint]):
        """Sets the bumpy_points of this FullProcessedTrip.


        :param bumpy_points: The bumpy_points of this FullProcessedTrip.
        :type bumpy_points: List[BumpyPoint]
        """

        self._bumpy_points = bumpy_points

    @property
    def gnss_data(self) -> List[TripGnssData]:
        """Gets the gnss_data of this FullProcessedTrip.


        :return: The gnss_data of this FullProcessedTrip.
        :rtype: List[TripGnssData]
        """
        return self._gnss_data

    @gnss_data.setter
    def gnss_data(self, gnss_data: List[TripGnssData]):
        """Sets the gnss_data of this FullProcessedTrip.


        :param gnss_data: The gnss_data of this FullProcessedTrip.
        :type gnss_data: List[TripGnssData]
        """

        self._gnss_data = gnss_data
