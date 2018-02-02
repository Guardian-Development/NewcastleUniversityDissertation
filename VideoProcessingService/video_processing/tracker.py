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
        self.object_trackers = []
        self.previous_detected_objects = []

    def init_object_trackers(self, 
                             frame: ndarray,
                             detected_objects: List[Tuple[float, float, float, float, str]]) -> None:
        self.object_trackers = [cv2.TrackerKCF_create() for _ in detected_objects]
        for tracker, detected_object in zip(self.object_trackers, detected_objects):
            ok = tracker.init(frame, detected_object[:4])
            if not ok:
                self.object_trackers.remove(tracker)
                print("FAILED to init tracker")
                

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
        
        # if we have never detected any objects before, init tracker for all
        if not self.object_trackers:
            self.init_object_trackers(frame, detected_objects)
            return detected_objects

        # are there any new objects to track?
        # - caclulate centre of bounding boxes 
        # - calculate distance to any of the tracked objects
        # - if distance is within a threshold then probably tracking same object and remove


        # build object locations based on tracking 
        tracked_objects = []
        for tracker in self.object_trackers: 
            ok, location = tracker.update(frame)
            if ok:
                x, y, width, height = location
                tracked_objects.append(
                    (int(x), int(y), int(x + width), int(y + height), "tracked_object"))
            else: 
                self.object_trackers.remove(tracker)

        self.previous_detected_objects = tracked_objects
        return self.previous_detected_objects
