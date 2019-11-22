# coding: utf-8

from __future__ import absolute_import

from swagger_server.test import BaseTestCase
from swagger_server import  mongodb_interface
import random

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

        filename = 'testfile'
        testdata = bytes(str(random.random()),'utf8')
        mongodb_interface.delete_file_by_filename(filename)
        mongodb_interface.insert_new_file(filename, testdata)
        gridOut = mongodb_interface.get_file_by_filename(filename)
        self.assertEqual(retrdata,testdata)
        mongodb_interface.delete_file_by_filename(filename)
        gridOut = mongodb_interface.get_file_by_filename(filename)
        self.assertEqual(gridOut, None)

if __name__ == '__main__':
    import unittest
    unittest.main()
