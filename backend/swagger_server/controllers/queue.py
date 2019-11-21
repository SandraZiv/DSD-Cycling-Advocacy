import pika
import json
import uuid
import road_analysis


# take a message and publish on the queue
# we also need a consumer subscribed to some queue read the messages and perform the jobs (see below)
# TODO use a dictionary for host name, queue name, etc
# TODO use a logger
def send_message(message, queue):
    connection = pika.BlockingConnection(pika.ConnectionParameters(host='localhost'))
    channel = connection.channel()
    channel.queue_declare(queue=queue)
    channel.basic_publish(exchange='', routing_key=queue, body=message)
    print(message)
    # print('[QUEUE] New message published on queue: %s with message_id: %s' % queue, json.loads(message)['message_id'])
    connection.close()
    return


# retrieve raw_data from the db, build a message and publish it on the correct queue
# # raw_data is the trip collected by android
# # action is what the consumer has to do (in this case, run trip analysis)
# # message_id is just a uuid, just in case
# TODO retrieve raw data from mongo, need db controller
# TODO use a dictionary for queue name, actions, etc
# TODO define the message, this is dumb
# TODO use a logger
def new_trip_analysis_job(trip_id):
    raw_data = {'trip_id': trip_id}
    queue = 'jobs'
    action = 'new-trip-analysis'
    message_id = uuid.uuid4().__str__()
    # build a message to be published on the queue. the consumer will read it and consume the action
    send_message(json.dumps({
        'message_id': message_id,
        'action': action,
        'raw_data': raw_data}), queue)
    print('[QUEUE] Enqueued new trip analysis job')
    return


def new_trip_analysis(raw_data):
    road_analysis.build_trip(raw_data)


actions = {"new-trip-analysis": new_trip_analysis}


def on_action(channel, method, properties, body):
    _body = json.loads(body)
    message_id = _body['message_id']
    action = _body['action']
    raw_data = _body['raw_data']
    # index of possible actions, can be move to global dictionary
    print('[QUEUE] Running road analyzer')
    actions.get(action, lambda: 'Invalid')(raw_data)
    print('[QUEUE] Exit road analyzer')


class QueueListener:

    def __init__(self, queue):
        self.queue = queue
        try:
            self.start_listening()
        except pika.exceptions.ChannelClosedByBroker:
            print('[QUEUE] No queue found')

    # when there is a new messages,
    # receive a message with an action name and some data
    # lookup for the corresponding action into the dictionary at top of this script and execute it (with params)
    def start_listening(self):
        connection = pika.BlockingConnection(pika.ConnectionParameters(host='localhost'))
        channel = connection.channel()
        channel.queue_declare(queue=self.queue)
        channel.basic_consume(queue=self.queue, on_message_callback=on_action, auto_ack=True)
        print('[QUEUE] New QueueListener subscribed to queue: %s' % self.queue)
        channel.start_consuming()
