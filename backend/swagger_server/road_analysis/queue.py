import pika
import json
import uuid
import logging
from threading import Thread
from pika.exceptions import AMQPConnectionError
from swagger_server.road_analysis import motion_data_analysis
from swagger_server import constants as const

"""
--- JOB PUBLISHING ---
To publish a new job on the queue: 
    import queue
    queue.Job(const.JOB_TYPE, data).enqueue_job(const.QUEUE_NAME)
This will send a message to a queue, containing data and the job type to be performed.

--- JOB CONSUMPTION ---
A listener has to be listening to a queue to consume jobs inside the queue.
    import queue
    queue.new_listener(const.QUEUE_NAME).
new_listener starts the listener on a new thread.
"""


# job type identify the callback function to be called by the listener
# data to be passed to the function (i.e. trip data) (or, better, tip_id, then retrieve trip from mongo)
class Job:

    def __init__(self, job_type, job_data):
        self.job_id = uuid.uuid4().__str__()
        self.job_type = job_type
        self.job_data = job_data

    # serialize the info and publish the message on the provided queue
    # TODO exceptions in case publishing fails
    def enqueue_job(self, queue):
        job = json.dumps({'id': self.job_id, 'type': self.job_type, 'data': self.job_data})
        connection = pika.BlockingConnection(pika.ConnectionParameters(host=const.RABBITMQ_HOST))
        channel = connection.channel()
        channel.queue_declare(queue=queue)
        channel.basic_publish(exchange='', routing_key=queue, body=job)
        logging.info('New job published on queue: %s' % queue)
        connection.close()
        return


# job getters are fundamental because jobs are json messages, and they need to be parsed

def get_job_type(serialized_job):
    return json.loads(serialized_job)['type']


def get_job_data(serialized_job):
    return json.loads(serialized_job)['data']


def get_job_id(serialized_job):
    return json.loads(serialized_job)['id']


# create a new listener inside a thread, listening to provided queue
def start_consuming(queue_name):
    # instantiate a listener for trip road_analysis queue on a new thread and test it
    new_thread = Thread(target=listen, args=(queue_name,))
    new_thread.start()
    return new_thread


# callback function for TRIP_ANALYSIS_JOB
def execute_trip_analysis_job(trip_uuid):
    logging.info('Executing trip analysis job for %s' % trip_uuid)
    motion_data_analysis.run_motion_data_analysis(trip_uuid)
    # map_update.run_map_update(trip_uuid)
    return


def execute_test_job(rubbish):
    logging.info(rubbish.__str__())
    return


# TODO exception to handle disconnection
def listen(queue_name):
    try:
        log = 'Started a new thread for queue listener\n'
        connection = pika.BlockingConnection(pika.ConnectionParameters(host=const.RABBITMQ_HOST))
        channel = connection.channel()
        channel.queue_declare(queue=queue_name)

        # jobs is an index of callback functions. basic_consume, below, read the job_type inside the messages
        # into the queue and calls the corresponding callback function
        callbacks = {
            const.TRIP_ANALYSIS_JOB: execute_trip_analysis_job,
            const.TEST_JOB: execute_test_job
        }
        # event manager for job execution
        channel.basic_consume(queue=queue_name,
                              on_message_callback=lambda c, m, p, body:
                              callbacks.get(get_job_type(body))(get_job_data(body)), auto_ack=True)

        log += 'New Listener subscribed to queue: %s\n' % queue_name
        log += 'Queue is up and running!\n'
        log += 'Queue verbosity is set to: %s' % const.VERBOSITY
        logging.info(log)
        channel.start_consuming()
    except AMQPConnectionError:
        logging.info('Could not run the queue. Maybe there is not RabbitMQ server running on this machine?')

