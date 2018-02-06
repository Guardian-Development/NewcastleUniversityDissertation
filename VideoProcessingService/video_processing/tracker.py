"""
Provides object tracking capabilities through multiple frames
"""
from typing import List
from collections import namedtuple
import cv2
import uuid
from numpy import ndarray
from video_processing.detector import Detector
from support.bounding_box import BoundingBox, bounding_boxes_collide, intersection_over_union

TrackingData = namedtuple("TrackingData", "uuid item_type")


class Tracker(Detector):
    """
    Allows tracking of objects through multiple frames
    """

    def __init__(self, object_detectors: List[Detector]):
        """
        Initialises a tracker using the given list of object detectors

        :param object_detectors: the detectors used to find the objects in the frame
        """

        self.object_detectors = object_detectors
        self.object_trackers = []

    def detect(self, frame: ndarray) -> List[BoundingBox]:
        """
        Detects objects within a frame
        Uses the object_detectors to find objects, allowing it to track them in future frames

        :param frame: the frame you wish to detect objects within
        :return: a list of coordinates of objects found in the image
        """

        detected_objects = []
        for detector in self.object_detectors:
            detected_objects.extend(detector.detect(frame))

        tracked_objects = self.get_tracked_object_locations(frame)
        new_objects = self.detect_new_objects_and_track(detected_objects, frame, tracked_objects)

        tracked_objects.extend(new_objects)
        return tracked_objects

    def detect_new_objects_and_track(self,
                                     detected_objects: List[BoundingBox],
                                     frame: ndarray,
                                     tracked_objects: List[BoundingBox]) -> List[BoundingBox]:
        """
        Detects new objects to track within the given frame and stats trackign them
        Does this by comparing them to existing tracked objects and detecting a collision percentage determining
        the likelihood we are already detecting that object

        :param detected_objects: all objects detected within the frame
        :param frame: the frame the objects were detected within
        :param tracked_objects: the currently tracked objects
        :return: a list of new objects that are now being tracked
        """
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
                    new_objects.append(
                        BoundingBox(detected_object.x_position,
                                    detected_object.y_position,
                                    detected_object.width,
                                    detected_object.height,
                                    detected_object.item_type,
                                    uuid.uuid4()))
        return new_objects

    def initialise_tracker_for_object(self, frame: ndarray, detected_object: BoundingBox) -> bool:
        """
        Initialises a tracker for a detected object within the given frame

        :param frame: the frame the object was detected in
        :param detected_object: the location of the object to track within the frame
        :return: true if we could initialise a tracker, else false
        """
        new_tracker = cv2.TrackerMIL_create()
        ok = new_tracker.init(frame,
                              (detected_object.x_position,
                               detected_object.y_position,
                               detected_object.width,
                               detected_object.height))
        if not ok:
            return False

        self.object_trackers.append((new_tracker, TrackingData(uuid.uuid4(), detected_object.item_type)))
        return True

    def get_tracked_object_locations(self, frame) -> List[BoundingBox]:
        """
        Builds a list of locations of objects based on the trackers for them

        :param frame: the frame to track the objects within
        :return: list of object locations tracked within the frame
        """
        tracked_objects = []
        for tracker, tracking_data in self.object_trackers:
            ok, location = tracker.update(frame)
            if ok:
                x, y, width, height = location
                tracked_objects.append(BoundingBox(x, y, width, height, tracking_data.item_type, tracking_data.uuid))
            else:
                self.object_trackers.remove((tracker, tracking_data))
        return tracked_objects
