"""Provides output based on the items detected in an image

Allow output of various formats based on the items detected in an image
"""
from typing import List, Tuple
from numpy import ndarray
import cv2

class VideoOutput:
    """Allows output of information based on a frame and the objects detected within it
    """

    def produce_output(self, frame: ndarray, detected_objects: List[Tuple[float, float, float, float, str]]) -> None:
        """Produces the desired output based on the frame and detected objects within it
        
        Arguments:
            frame: ndarray {[ndarray]} -- [the frame that has been processed]
            detected_objects: List[Tuple[float, float, float, float, str]]
                -- the x and y coordinates of the bottom left corner, then the x + width and y + height
                   the str is the name of the detected object type
        
        Raises:
            NotImplementedError -- should be implemented in child classes
        """
        raise NotImplementedError

class LocalDisplayVideoOutput(VideoOutput):
    """Displays the frame to screen with rectangles around detected objects using opencv

    Different colours are used for different types
    """

    def produce_output(self, frame: ndarray, detected_objects: List[Tuple[float, float, float, float, str]]) -> None:
        """Places rectangles around all detected objects, colouring them by type 

        Supported types are: 
            person - colour green 
            car - colour blue
        
        Arguments:
            frame: ndarray {[ndarray]} -- [the frame that has been processed]
            detected_objects: List[Tuple[float, float, float, float, str]]
                -- the x and y coordinates of the bottom left corner, then the x + width and y + height
                   the str is the name of the detected object type
        """
        for (x, y, x_plus_width, y_plus_height, event_type) in detected_objects:
            colour = (0, 255, 0) if event_type == "person" else (0, 0, 255)
            cv2.rectangle(frame, (x, y), (x_plus_width, y_plus_height), colour, 2)
        cv2.imshow('Video', frame)
