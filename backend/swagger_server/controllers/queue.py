import pika
import json
import uuid
import logging
from swagger_server.controllers import constants as const
from swagger_server.controllers import road_analysis

logging.basicConfig(filename='log.log', level=logging.DEBUG)


# --- JOBS ---

def get_job_type(serialized_job):
    return json.loads(serialized_job)['type']


def get_job_data(serialized_job):
    return json.loads(serialized_job)['data']


def get_job_id(serialized_job):
    return json.loads(serialized_job)['id']


class Job:

    def __init__(self, job_type, job_data):
        self.job_id = uuid.uuid4().__str__()
        self.job_type = job_type
        self.job_data = job_data

    def enqueue_job(self, queue):
        job = json.dumps({'id': self.job_id, 'type': self.job_type, 'data': self.job_data})
        connection = pika.BlockingConnection(pika.ConnectionParameters(host=const.RABBITMQ_HOST))
        channel = connection.channel()
        channel.queue_declare(queue=queue)
        channel.basic_publish(exchange='', routing_key=queue, body=job)
        logging.info('[QUEUE] New message published on queue')
        connection.close()
        return


# --- JOBS LISTENER ---

def new_listener(queue_name):
    listener = QueueListener(queue_name)
    try:
        listener.listen()
    except pika.exceptions.ChannelClosedByBroker:
        logging.info('[QUEUE] No queue found')
        return False
    return


def test_queue(queue_name):
    Job(job_type=const.TEST_JOB, job_data='  <°(((>>  ').enqueue_job(queue_name)
    logging.info('Sent some fish to queue: %s' % queue_name)
    return


def execute_trip_analysis_job(data):
    road_analysis.run_trip_analysis(data)
    return


def execute_test_job(data):
    logging.info('Received something. If you see a fish: %s  this means that the queue is working!' % data)
    return


class QueueListener:

    def __init__(self, queue):
        self.queue = queue

    def listen(self):
        connection = pika.BlockingConnection(pika.ConnectionParameters(host=const.RABBITMQ_HOST))
        channel = connection.channel()
        channel.queue_declare(queue=self.queue)
        # THIS IS THE QUEUE CONSUMER ENGINE
        # jobs is an index of callback functions to be passed below to basic_consume
        callbacks = {
            const.TRIP_ANALYSIS_JOB: execute_trip_analysis_job,
            const.TEST_JOB: execute_test_job
        }
        # consume items in the queue by passing the function indicated into message.job
        channel.basic_consume(queue=self.queue,
                              on_message_callback=lambda c, m, p, body:
                              callbacks.get(get_job_type(body))(get_job_data(body)), auto_ack=True)
        logging.info('[QUEUE] New QueueListener subscribed to queue: %s' % self.queue)
        channel.start_consuming()
