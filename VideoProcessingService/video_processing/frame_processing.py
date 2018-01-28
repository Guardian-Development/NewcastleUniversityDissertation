"""Provides image processing capabilities

Goal is to apply filters/transforms to images
"""

from numpy import ndarray
import cv2
from imutils import resize

class FrameProcessor:
    """Processes an individual frame

    Provides access to process an individual frame returning a frame with the transform applied
    """


    def process_frame(self, frame: ndarray) -> ndarray:
        """Processes an individual frame returning the result as a new frame
        
        Arguments:
            frame: ndarray {[ndarray]} -- [the frame which you want to apply the transform to]
        
        Raises:
            NotImplementedError -- should be implemented in child classes
        """

        raise NotImplementedError

class BlackAndWhiteFrameProcessor(FrameProcessor):
    """Applies a black and white filter to the frame

    Makes use of open_cv to apply a black and white filter
    """

    def process_frame(self, frame: ndarray) -> ndarray:
        """Applies a black and white filter
        
        Arguments:
            frame: ndarray {[ndarray]} -- [the frame to apply the black and white filter to]
        
        Returns:
            [ndarray] -- [the new frame with the black and white filter applied]
        """

        return cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

class ResizeFrameProcessor(FrameProcessor):
    """Resizes a frame to a given size
    
    Provides ability to make sure a frame is never above a certain size
    """

    def __init__(self, width: int, height: int):
        """Initialises the class using the width and height as the frame resize contraints
        
        Arguments:
            width: int {[int]} -- [the max width the frame can be]
            height: int {[int]} -- [the max height the frame can be]
        """

        self.width = width
        self.height = height

    def process_frame(self, frame: ndarray) -> ndarray:
        """Transforms the frame by resizing it to within a given range
        
        Arguments:
            frame: ndarray {[ndarray]} -- [the frame to apply the resizing to]
        
        Returns:
            [ndarray] -- [the frame resized to be no larger than the width and height specified]
        """

        return resize(frame, width=self.width, height=self.height)
