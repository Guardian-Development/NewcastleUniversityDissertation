"""
Parses command line arguments to build applications configuration
"""
import argparse
from argparse import Namespace


def process_command_line_arguments() -> Namespace:
    """
    Processes command line arguments to build valid program configuration

    :return: object with simple properties for each command line argument
    """

    parser = build_parser()
    arguments = parser.parse_args()

    return arguments


def build_parser() -> argparse.ArgumentParser:
    """
    Builds up the command line parser options

    :return: The built command line parser
    """

    parser = argparse.ArgumentParser(
        description="Kafka support tool that writes a Kafka topic to file in json format")
    parser.add_argument(
        "kafkaurl",
        help="The Kafka connection url to connect to",
        type=str
    )
    parser.add_argument(
        "kafkatopic",
        help="The Kafka topic you wish to read from",
        type=str
    )
    return parser
