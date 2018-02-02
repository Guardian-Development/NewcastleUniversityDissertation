"""Provides object tracking capabilities through multiple frames
"""

from typing import List, Tuple
import cv2
from math import hypot
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

    def init_object_trackers(self, 
                             frame: ndarray,
                             detected_objects: List[Tuple[float, float, float, float, str]]) -> None:
        self.object_trackers = [cv2.TrackerKCF_create() for _ in detected_objects]
        for tracker, detected_object in zip(self.object_trackers, detected_objects):
            ok = tracker.init(frame, detected_object[:4])
            if not ok:
                self.object_trackers.remove(tracker)
                print("FAILED to init tracker")

    def bounding_boxes_collide(self,
                               box1: Tuple[float, float, float, float],
                               box2: Tuple[float, float, float, float]) -> bool:
        return False

    def intersection_over_union(self,
                                box1: Tuple[float, float, float, float],
                                box2: Tuple[float, float, float, float]) -> int: 
        return 0
                

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
        
        new_objects = []
        for detected_object in detected_objects: 
            for tracked_object, tracker in zip(tracked_objects, self.object_trackers): 

                # if rectangles collide then lets see how much by
                if self.bounding_boxes_collide(detected_object[:4], tracked_object): 
                    collision_amount = self.intersection_over_union(detected_object[:4], tracked_object)
                    
                    # if they breach theshold of collision, they are likely the same thing
                    # therefore take the detected object over tracker as source of truth
                    if collision_amount > 0.5: 
                        self.object_trackers.remove(tracker)
                        new_tracker = cv2.TrackerKCF_create()
                        ok = new_tracker.init(frame, detected_object[:4])
                        if not ok:
                            self.object_trackers.remove(new_tracker)
                            print("FAILED to init tracker")
                            continue
                        new_objects.append(detected_object)

        return tracked_objects.extend(new_objects)
