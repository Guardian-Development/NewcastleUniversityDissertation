"""Provides object tracking capabilities through multiple frames
"""

from typing import List, Tuple
import cv2
from numpy import ndarray
from video_processing.detector import Detector

class Tracker(Detector):
    """Allows tracking of objects through multiple frames

    Makes use of the opencv tracking library
    """

    def __init__(self, object_detectors: List[Detector]):
        """Initialises a tracker using the given list of object detectors
        
        Arguments:
            object_detectors: List[Detector] {[Detector]}
                -- [the detectors used to find the objects in the frame]
        """

        self.object_detectors = object_detectors

    def detect(self, frame: ndarray) -> List[Tuple[float, float, float, float, str]]:
        """Detects objects within a frame

        Uses the object_detectors to find objects, allowing it to track them in future frames
        
        Arguments:
            frame: ndarray {[ndarray]} -- [the frame you wish to detect objects within]

        Returns:
            [List[Tuple[float, float, float, float]]] -- [a list of coordinates that people are found at in the image]
        """

        detected_objects = []
        for detector in self.object_detectors: 
            detected_objects.extend(detector.detect(frame))
        trackers = [cv2.TrackerKCF_create() for _ in detected_objects]

        # # Initialize tracker with first frame and bounding box
        # ok = tracker.init(frame, bbox)
 
        # while True:
        #     # Read a new frame
        #     ok, frame = video.read()
        #     if not ok:
        #         break
            
        #     # Start timer
        #     timer = cv2.getTickCount()
    
        #     # Update tracker
        #     ok, bbox = tracker.update(frame)

        return detected_objects
