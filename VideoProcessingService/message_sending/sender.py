"""
Provides the ability to send messages to external sources
This service can be used to send information to server, for instance through Apache Kafka
"""
from typing import Any, List
import json
from kafka import KafkaProducer
from support.bounding_box import BoundingBox, convert_to_dict


class MessageSender:
    """
    Provides the ability to send a message of any type
    """

    def send_message(self, information: Any) -> None:
        """
        Provides the ability to send an individual message
        :param information: the information you need to send the message
        :return:
        """
        raise NotImplementedError


class ApacheKafkaMessageSender(MessageSender):
    """
    Provides the ability to send messages to an Apache Kafka topic
    """

    def __init__(self, server_address: str, topic: str):
        """
        Initialises this message sender for a given topic

        :param server_address: the server address of the kafka instance you wish to send to
        :param topic: the topic name you wish to send to
        """
        self.producer = KafkaProducer(
            bootstrap_servers=[server_address],
            value_serializer=lambda m: json.dumps(m).encode('ascii'),
            api_version=(0, 10, 1))
        self.topic = topic

    def send_message(self, information: List[BoundingBox]) -> None:
        """
        Sends a message for a list of detected objects through Apache Kafka

        :param information: list of object locations you wish to send
        :return: None
        """
        json_message = convert_bounding_box_to_dict(information)
        future = self.producer.send(self.topic, json_message)
        future.get(timeout=0.2)


def convert_bounding_box_to_dict(objects: List[BoundingBox]) -> dict:
    """
    Converts a list of BoundingBox's to a dict

    :param objects: the list of BoundingBox's you wish to convert
    :return: a dictionary representation of the objects
    """
    json_message = {}
    built_objects = []
    for detected_object in objects:
        json_detected_object = convert_to_dict(detected_object)
        built_objects.append(json_detected_object)
    json_message["detected_objects"] = built_objects
    return json_message
