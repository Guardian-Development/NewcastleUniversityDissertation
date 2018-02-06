"""Provides output based on the items detected in an image

Allow output of various formats based on the items detected in an image
"""
from typing import List
from numpy import ndarray
from support.bounding_box import BoundingBox
import cv2


class VideoOutput:
    """Allows output of information based on a frame and the objects detected within it
    """

    def produce_output(self, frame: ndarray, detected_objects: List[BoundingBox]) -> None:
        """Produces the desired output based on the frame and detected objects within it
        
        Arguments:
            frame: ndarray {[ndarray]} -- [the frame that has been processed]
            detected_objects: List[BoundingBox] -- [the list of detected objects within the frame]
        
        Raises:
            NotImplementedError -- should be implemented in child classes
        """
        raise NotImplementedError


class LocalDisplayVideoOutput(VideoOutput):
    """Displays the frame to screen with rectangles around detected objects using opencv

    """

    def produce_output(self, frame: ndarray, detected_objects: List[BoundingBox]) -> None:
        """Places rectangles around all detected objects
        
        Arguments:
            frame: ndarray {[ndarray]} -- [the frame that has been processed]
            detected_objects: List[BoundingBox] -- [the list of detected objects within the frame]
        """
        print(detected_objects)
        for x, y, width, height, item_type, _ in detected_objects:
            colour = (0, 255, 0)
            cv2.rectangle(frame, (int(x), int(y)), (int(x + width), int(y + height)), colour, 2)
        cv2.imshow('Video', frame)
