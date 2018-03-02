"""
Provides a Kafka Consumer that can read json messages off a Kafka topic
"""
from typing import Dict, List
from kafka import KafkaConsumer
import json


class KafkaJsonMessageConsumer(object):
    """
    Provides a Kafka Consumer that can read json messages placed on a Kafka topic
    Decodes them from json using the json library
    """

    def __init__(self, kafka_urls: List[str], kafka_topic: str):
        """
        Initialises the consumer for the given kafka broker urls and topic
        :param kafka_urls: the broker urls to connect to
        :param kafka_topic: the topic to read from
        """
        self.consumer = KafkaConsumer(bootstrap_servers=kafka_urls,
                                      auto_offset_reset='earliest',
                                      consumer_timeout_ms=1000)
        self.consumer.subscribe([kafka_topic])

    def consume_messages_in_topic(self) -> Dict:
        """
        Reads all messages from the subscribed topic
        :return: a list of messages read from the Kafka topic
        """
        messages = {'ordered_messages': []}
        for message in self.consumer:
            messages['ordered_messages'].append(json.loads(message.value.decode("utf-8")))
        return messages
