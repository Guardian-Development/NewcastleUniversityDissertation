"""Provides the ability to send messages to external sources

This service can be used to send information to server, for instance through Apache Kafka
"""
from typing import Any, List
import json
from kafka import KafkaProducer
from support.bounding_box import BoundingBox, convert_to_dict


class MessageSender:
    """Provides the ability to send a message of any type
    """

    def send_message(self, message: Any) -> None:
        """Provides the ability to send an individual message
        
        Arguments:
            message: Any {[any]} -- [the message you wish to send]

        Raises:
            NotImplementedError -- should be implemented in child classes
        """
        raise NotImplementedError


class ApacheKafkaMessageSender(MessageSender):
    """Provides the ability to send messages to an Apache Kafka server

    Each initialised message sender can send to a specific topic of the Apache Kafka server
    """

    def __init__(self, server_address: str, topic: str):
        """Initialises this message sender for a given topic
        
        Arguments:
            server_address: str {[str]}
                -- [the server address of the kafka instance you wish to send to]
            topic: str {[str]} -- [the topic name you wish to send to]
        """

        self.producer = KafkaProducer(
            bootstrap_servers=[server_address],
            value_serializer=lambda m: json.dumps(m).encode('ascii'),
            api_version=(0, 10, 1))
        self.topic = topic

    def send_message(self, message: List[BoundingBox]) -> None:
        """Sends a message for a list of detected objects through Apache Kafka
        
        Arguments:
            message: List[BoundingBox] -- [list of object locations you wish to send]
        """

        json_message = convert_messages_to_dict(message)
        future = self.producer.send(self.topic, json_message)
        future.get(timeout=0.2)


def convert_messages_to_dict(message: List[BoundingBox]) -> dict:
    """Converts a message in the form of a list of object locations to a dictionary
    
     Arguments:
        message: message: List[BoundingBox] -- [list of object locations you wish to convert to json]
    
    Returns:
        [dict] -- [a json serializable dictionary that represents the message]
    """

    json_message = {}
    built_objects = []
    for detected_object in message:
        json_detected_object = convert_to_dict(detected_object)
        built_objects.append(json_detected_object)
    json_message["detected_objects"] = built_objects
    return json_message
