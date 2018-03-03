"""
Responsible for sending messages to a Kafka instance
"""

from typing import List
from kafka import KafkaProducer
import json


class KafkaJsonMessageProducer(object):
    """
    Sends JSON messages to a Kafka instance
    """

    def __init__(self, kafka_urls: List[str], kafka_topic: str):
        """
        Initialises the Kafka Producer for the kafka_urls and kafka_topic supplied
        :param kafka_urls: the broker connection URLs for Kafka
        :param kafka_topic: the topic you wish to publish to
        """
        self.producer = KafkaProducer(value_serializer=lambda v: json.dumps(v).encode('utf-8'),
                                      bootstrap_servers=kafka_urls)
        self.topic = kafka_topic

    def produce_message(self, message: str):
        """
        Sends a single message to kafka and awaits the response
        :param message: the message to send
        """
        future = self.producer.send(self.topic, message)
        future.get(timeout=60)
