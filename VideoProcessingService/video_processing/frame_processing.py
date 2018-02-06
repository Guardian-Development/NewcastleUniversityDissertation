"""
Provides image processing capabilities
Goal is to apply filters/transforms to images
"""

from numpy import ndarray
import cv2
from imutils import resize


class FrameProcessor:
    """
    Processes an individual frame
    Provides access to process an individual frame returning a frame with the transform applied
    """

    def process_frame(self, frame: ndarray) -> ndarray:
        """
        Processes an individual frame returning a new frame with the process applied

        :param frame: the frame to process
        :return: the frame after being processed
        """

        raise NotImplementedError


class BlackAndWhiteFrameProcessor(FrameProcessor):
    """
    Applies a black and white filter to the frame
    """

    def process_frame(self, frame: ndarray) -> ndarray:
        return cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)


class ResizeFrameProcessor(FrameProcessor):
    """
    Resize's a frame to a given size
    """

    def __init__(self, width: int, height: int):
        """
        Initialises the class to use the width and height constraints provided

        :param width: the max width the frame can be
        :param height: the max height the frame can be
        """

        self.width = width
        self.height = height

    def process_frame(self, frame: ndarray) -> ndarray:
        return resize(frame, width=self.width, height=self.height)
