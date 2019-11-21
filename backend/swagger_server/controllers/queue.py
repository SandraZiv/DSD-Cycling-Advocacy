import pika
import json
import uuid


# take a message and publish on the queue
# we also need a consumer subscribed to some queue read the messages and perform the jobs (see below)
# TODO use a dictionary for host name, queue name, etc
# TODO use a logger
def send_message(message, queue):
    routing_key = queue

    connection = pika.BlockingConnection(pika.ConnectionParameters(host='localhost'))
    channel = connection.channel()
    channel.queue_declare(queue=routing_key)
    channel.basic_publish(exchange='', routing_key='routing_key', body=message)

    print('[QUEUE] New message published on queue: '+routing_key+' with message_id: ' + message.header[0].message_id)
    print(message)

    connection.close()

    return True


# retrieve raw_data from the db, build a message and publish it on the correct queue
# # raw_data is the trip collected by android
# # action is what the consumer has to do (in this case, run trip analysis)
# # message_id is just a uuid, just in case
# TODO retrieve raw data from mongo, need db controller
# TODO use a dictionary for queue name, actions, etc
# TODO define the message, this is dumb
# TODO use a logger
def new_trip_analysis_job(trip_id):
    raw_data = {}
    queue = 'dummy-queue-name'
    action = 'dummy-action'
    message_id = uuid.uuid4()

    # build a message to be published on the queue. the consumer will read it and consume the action
    message = {
        'header': [{'message_id': message_id}, {'action': action}],
        'body': raw_data}

    send_message(json.dumps(message), queue)

    print('[QUEUE] Enqueued new trip analysis job')

    return True


actions = {
    "new-trip-analysis": new_trip_analysis_job
}


class QueueListener:

    def __init__(self, queue):
        self.queue = queue

    # when there is a new messages,
    # receive a message with an action name and some data
    # lookup for the corresponding action into the dictionary at top of this script and execute it (with params)
    def start_listening(self):
        connection = pika.BlockingConnection(pika.ConnectionParameters(host='localhost'))
        channel = connection.channel()
        channel.queue_declare(queue=self.queue)

        def callback(ch, method, properties, body):
            actions.get(body.action, lambda: 'Invalid')(body.params)
        channel.basic_consume(queue='hello', on_message_callback=callback, auto_ack=True)
        print('[QUEUE] New QueueListener subscribed to queue: %s' % self.queue)
        channel.start_consuming()

    start_listening()
