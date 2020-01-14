# coding: utf-8

from __future__ import absolute_import
from datetime import date, datetime  # noqa: F401

from typing import List, Dict  # noqa: F401

from swagger_server.models.base_model_ import Model
from swagger_server import util


class Body(Model):
    """NOTE: This class is auto generated by the swagger code generator program.

    Do not edit the class manually.
    """
    def __init__(self, file: str=None, trip_uuid: str=None):  # noqa: E501
        """Body - a model defined in Swagger

        :param file: The file of this Body.  # noqa: E501
        :type file: str
        :param trip_uuid: The trip_uuid of this Body.  # noqa: E501
        :type trip_uuid: str
        """
        self.swagger_types = {
            'file': str,
            'trip_uuid': str
        }

        self.attribute_map = {
            'file': 'file',
            'trip_uuid': 'tripUUID'
        }
        self._file = file
        self._trip_uuid = trip_uuid

    @classmethod
    def from_dict(cls, dikt) -> 'Body':
        """Returns the dict as a model

        :param dikt: A dict.
        :type: dict
        :return: The body of this Body.  # noqa: E501
        :rtype: Body
        """
        return util.deserialize_model(dikt, cls)

    @property
    def file(self) -> str:
        """Gets the file of this Body.


        :return: The file of this Body.
        :rtype: str
        """
        return self._file

    @file.setter
    def file(self, file: str):
        """Sets the file of this Body.


        :param file: The file of this Body.
        :type file: str
        """

        self._file = file

    @property
    def trip_uuid(self) -> str:
        """Gets the trip_uuid of this Body.


        :return: The trip_uuid of this Body.
        :rtype: str
        """
        return self._trip_uuid

    @trip_uuid.setter
    def trip_uuid(self, trip_uuid: str):
        """Sets the trip_uuid of this Body.


        :param trip_uuid: The trip_uuid of this Body.
        :type trip_uuid: str
        """

        self._trip_uuid = trip_uuid