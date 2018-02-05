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

        box1_left_x, box1_bottom_y, box1_right_x, box1_top_y = box1
        box2_left_x, box2_bottom_y, box2_right_x, box2_top_y = box2

        return (box1_left_x < box2_right_x and
                box1_right_x > box2_left_x and
                box1_top_y > box2_bottom_y and
                box1_bottom_y < box2_top_y)

    def bounding_box_inclusive(self,
                               box1: Tuple[float, float, float, float],
                               box2: Tuple[float, float, float, float]) -> bool:
        box1_left_x, box1_bottom_y, box1_right_x, box1_top_y = box1
        box2_left_x, box2_bottom_y, box2_right_x, box2_top_y = box2

        return (box1_left_x <= box2_left_x and
                box1_right_x <= box2_right_x and
                box1_top_y <= box2_top_y and
                box1_bottom_y <= box2_bottom_y)

    def intersection_over_union(self,
                                box1: Tuple[float, float, float, float],
                                box2: Tuple[float, float, float, float]) -> float:
        box1_left_x, box1_bottom_y, box1_right_x, box1_top_y = box1
        box2_left_x, box2_bottom_y, box2_right_x, box2_top_y = box2

        intersection_left_x = max(box1_left_x, box2_left_x)
        intersection_bottom_y = max(box1_bottom_y, box2_bottom_y)
        intersection_right_x = min(box1_right_x, box2_right_x)
        intersection_top_y = min(box1_top_y, box2_top_y)

        intersection_area = (intersection_right_x - intersection_left_x + 1) * \
                            (intersection_top_y - intersection_bottom_y + 1)

        box1_area = (box1_right_x - box1_left_x + 1) * \
                    (box1_top_y - box1_bottom_y + 1)
        box2_area = (box2_right_x - box2_left_x + 1) * \
                    (box2_top_y - box2_bottom_y + 1)

        return intersection_area / float(box1_area + box2_area - intersection_area)

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

            is_new_object = True

            for tracked_object, tracker in zip(tracked_objects, self.object_trackers):

                # if rectangles collide then lets see how much by
                if self.bounding_boxes_collide(detected_object[:4], tracked_object[:4]):
                    collision_amount = self.intersection_over_union(detected_object[:4], tracked_object[:4])
                    if collision_amount > 0.03:
                        is_new_object = False
                        tracked_objects.remove(tracked_object)
                        self.object_trackers.remove(tracker)
                        new_tracker = cv2.TrackerKCF_create()
                        ok = new_tracker.init(frame, detected_object[:4])
                        if not ok:
                            continue
                        self.object_trackers.append(new_tracker)
                        new_objects.append(detected_object)
                        break

            if is_new_object:
                new_tracker = cv2.TrackerKCF_create()
                ok = new_tracker.init(frame, detected_object[:4])
                if not ok:
                    continue
                self.object_trackers.append(new_tracker)
                new_objects.append(detected_object)

        # check if a tracked object is inclusive of another, remove the least specific object
        for tracked_object, tracker in zip(tracked_objects, self.object_trackers):
            for new_object in new_objects:
                if self.bounding_box_inclusive(new_object[:4], tracked_object[:4]):
                    tracked_objects.remove(tracked_object)
                    self.object_trackers.remove(tracker)
                    break

        return tracked_objects + new_objects
