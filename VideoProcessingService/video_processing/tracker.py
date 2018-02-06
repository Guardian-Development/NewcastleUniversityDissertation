"""Provides object tracking capabilities through multiple frames
"""

from typing import List
import cv2
from numpy import ndarray
from video_processing.detector import Detector
from support.bounding_box import BoundingBox, bounding_boxes_collide, intersection_over_union


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

    def detect(self, frame: ndarray) -> List[BoundingBox]:
        """Detects objects within a frame

        Uses the object_detectors to find objects, allowing it to track them in future frames
        
        Arguments:
            frame: ndarray {[ndarray]} -- [the frame you wish to detect objects within]

        Returns:
            [List[BoundingBox]] -- [a list of coordinates that people are found at in the image]
        """

        detected_objects = []
        for detector in self.object_detectors:
            detected_objects.extend(detector.detect(frame))

        tracked_objects = self.get_tracked_object_locations(frame)
        new_objects = self.detect_new_objects_and_track(detected_objects, frame, tracked_objects)

        tracked_objects.extend(new_objects)
        return tracked_objects

    def detect_new_objects_and_track(self, detected_objects, frame, tracked_objects) -> List[BoundingBox]:
        new_objects = []

        for detected_object in detected_objects:
            is_new_object = True

            # can we find something we already track that shows this is not new
            for tracked_object in tracked_objects:
                if bounding_boxes_collide(detected_object, tracked_object):
                    collision_amount = intersection_over_union(detected_object, tracked_object)
                    if collision_amount > 0.2:
                        is_new_object = False
                        break

            if is_new_object:
                could_track = self.initialise_tracker_for_object(frame, detected_object)
                if could_track:
                    new_objects.append(detected_object)

        return new_objects

    def initialise_tracker_for_object(self, frame: ndarray, detected_object: BoundingBox) -> bool:
        new_tracker = cv2.TrackerMIL_create()
        ok = new_tracker.init(frame, (
            detected_object.x_position,
            detected_object.y_position,
            detected_object.width,
            detected_object.height))
        if not ok:
            return False

        self.object_trackers.append(new_tracker)
        return True

    def get_tracked_object_locations(self, frame) -> List[BoundingBox]:
        tracked_objects = []
        for tracker in self.object_trackers:
            ok, location = tracker.update(frame)
            if ok:
                x, y, width, height = location
                tracked_objects.append(BoundingBox(x, y, width, height, "tracked_object"))
            else:
                self.object_trackers.remove(tracker)
        return tracked_objects
