"""Provides the ability to send messages to externak sources

This service can be used to send information to server, for instance through Apache Kafka
"""
from typing import Any, List, Tuple
import json
from kafka import KafkaProducer


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

    def send_message(self, message: List[Tuple[float, float, float, float, str]]) -> None:
        """Sends a message for a list of detected objects through Apache Kafka
        
        Arguments:
            message: List[Tuple[float {[float]} -- [x coordinate]
            float {[float]} -- [y coordinate]
            float {[float]} -- [x + width coordinate]
            float {[float]} -- [y + height coordinate]
            str]] {[str]} -- [detected object type]
        """
        json_message = convert_message_to_json(message)
        future = self.producer.send(self.topic, json_message)
        future.get(timeout=0.2)


def convert_message_to_json(message: List[Tuple[float, float, float, float, str]]) -> str:
    """Converts a message in the form of a list of object locations to a json string
    
     Arguments:
        message: List[Tuple[float {[float]} -- [x coordinate]
        float {[float]} -- [y coordinate]
        float {[float]} -- [x + width coordinate]
        float {[float]} -- [y + height coordinate]
        str]] {[str]} -- [detected object type]
    
    Returns:
        [str] -- [the json representation of the list of objects]
    """

    json_message = {}
    built_objects = []
    for detected_object in message:
        json_detected_object = {
            "x": detected_object[0],
            "y": detected_object[1],
            "x_plus_width": detected_object[2],
            "y_plus_height": detected_object[3],
            "type": detected_object[4]}
        built_objects.append(json_detected_object)
    json_message["detected_objects"] = built_objects
    return json_message
