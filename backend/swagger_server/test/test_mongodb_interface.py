# coding: utf-8

from __future__ import absolute_import

from swagger_server.test import BaseTestCase
from swagger_server import  mongodb_interface


class TestMongoDbInterface(BaseTestCase):
    """
    MongoDbInterface integration test stubs
    """

    def test_whole_interface(self):
        """
        Test the interface by deleting a test file if it exists,  inserting it with test data,
        querying it , validating the data, deleting the file, querying it again and validating 
        that it is empty
        """



def randomString(stringLength=10):
    """Generate a random string of fixed length """
    letters = string.ascii_lowercase
    return ''.join(random.choice(letters) for i in range(stringLength))

        filename = 'testfile'
        testdata = str(random.random())
        mongodb_interface.delete_file_by_filename(filename)
        mongodb_interface.insert_new_file(filename, testdata)
        mongodb_interface.get_file_by_filename(filename)
        mongodb_interface.delete_file_by_filename(filename)

        query_string = [('trip_uuid', '38400000-8cf0-11bd-b23e-10b96e4ef00d')]
        response = self.client.open(
            '/v1/trip/getTripByTripUUID',
            method='GET',
            query_string=query_string)
        self.assert200(response,
                       'Response body is : ' + response.data.decode('utf-8'))


if __name__ == '__main__':
    import unittest
    unittest.main()
