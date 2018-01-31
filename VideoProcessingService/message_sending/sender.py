"""Provides the ability to send messages to externak sources

This service can be used to send information to server, for instance through Apache Kafka
"""
from typing import Any
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

    def send_message(self, message: Any) -> None:
        """Send a message to the Apache Kafka server condigured when initialisng this class
        
        Arguments:
            message: Any {[any]} -- [send the given message to the server]
        """
        future = self.producer.send(self.topic, message)
        future.get(timeout=0.1)
