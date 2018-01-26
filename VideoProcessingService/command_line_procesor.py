"""Parses command line arguments

Parses command line arguments to build applications configuration
"""
import argparse
from argparse import Namespace
from enum import Enum

class VideoProcesingType(Enum):
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

    parser = argparse.ArgumentParser(
        description="Video processing tool for real-time or post-processing video streams.")
    parser.add_argument(
        "videosource",
        help="video source, webcam or file",
        type=VideoProcesingType,
        choices=list(VideoProcesingType)
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
    arguments = parser.parse_args()

    if arguments.videosource == VideoProcesingType.file and arguments.filelocation is None:
        parser.error("--video-source file requires --file-location to be specified")
    if arguments.videosource == VideoProcesingType.webcam and arguments.webcam is None:
        parser.error("--video-source cam requires --webcam to be specified")
    
    return arguments
