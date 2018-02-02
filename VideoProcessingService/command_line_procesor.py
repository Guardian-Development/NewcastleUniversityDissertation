"""Parses command line arguments

Parses command line arguments to build applications configuration
"""
import argparse
from argparse import Namespace
from enum import Enum


class VideoProcessingType(Enum):
    """Provides video processing types, such as webcam and file
    """
    webcam = 'cam'
    file = 'file'

    def __str__(self):
        return self.value


def process_command_line_arguments() -> Namespace:
    """Processes command line arguments to build valid program configuration
     
     Returns:
            [Namespace] -- [object with simple properties for each command line argument]
    """

    parser = build_parser()
    arguments = parser.parse_args()

    if arguments.videosource == VideoProcessingType.file and arguments.filelocation is None:
        parser.error("videosource file requires --file-location to be specified")
    if arguments.videosource == VideoProcessingType.webcam and arguments.webcam is None:
        parser.error("videosource cam requires --webcam to be specified")
    if arguments.kafkaurl is not None and arguments.kafkatopic is None:
        parser.error("--kafkaurl requres --kafkatopic to be specified")
    if arguments.kafkatopic is not None and arguments.kafkaurl is None:
        parser.error("--kafkatopic requres --kafkaurl to be specified")

    return arguments


def build_parser() -> argparse.ArgumentParser:
    """Builds up the command line parser options
    
    Returns:
        [argparse.ArgumentParser] -- [The built command line parser]
    """

    parser = argparse.ArgumentParser(
        description="Video processing tool for real-time or post-processing video streams.")
    parser.add_argument(
        "videosource",
        help="video source, webcam or file",
        type=VideoProcessingType,
        choices=list(VideoProcessingType)
    )
    parser.add_argument(
        "-l",
        "--filelocation",
        help="The file path of the video you want to process",
        type=str
    )
    parser.add_argument(
        "-w",
        "--webcam",
        help="The webcam input number to use",
        type=int
    )
    parser.add_argument(
        "-k",
        "--kafkaurl",
        help="The Kafka url you wish to publish messages to",
        type=str,
        default=None
    )
    parser.add_argument(
        "-t",
        "--kafkatopic",
        help="The Kafka topic you wish to publish messages to",
        type=str,
        default=None
    )
    return parser
