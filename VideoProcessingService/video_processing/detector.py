"""
Provides detection of different elements within a frame
"""
from typing import List
from numpy import ndarray
import numpy as np
import cv2
from imutils.object_detection import non_max_suppression
from support.bounding_box import BoundingBox


class Detector:
    """
    Detects a specific feature/object within a frame
    """

    def detect(self, frame: ndarray) -> List[BoundingBox]:
        """
        Detects a specific feature/object within a frame,
        and returns a list of locations the feature/object was detected

        :param frame: the frame to detect the feature/object within
        :return: a list of locations the feature/object occured
        """
        raise NotImplementedError


class PersonDetector(Detector):
    """
    Detects people within a frame
    """

    def __init__(self):
        """
        Initialises the class to use a Hog detection model trained to find people
        """
        hog = cv2.HOGDescriptor()
        hog.setSVMDetector(cv2.HOGDescriptor_getDefaultPeopleDetector())
        self.hog_detector = hog

    def detect(self, frame: ndarray) -> List[BoundingBox]:
        """
        Detects people in a frame and returns a list of locations they are found

        It makes use of opencv for initial person detection,
        then uses non_max_suppression function to help merge false positives

        :param frame: the frame to detect people within
        :return: the list of locations people were found in the frame
        """
        rectangles, weights = self.hog_detector.detectMultiScale(frame, winStride=(4, 4), padding=(32, 32), scale=1.05)
        rectangles = [r for (r, w) in zip(rectangles, weights) if w > 0.7]

        initial_people = np.array([[x, y, x + w, y + h] for (x, y, w, h) in rectangles])
        final_detected_people = non_max_suppression(initial_people, probs=None, overlapThresh=0.65)

        return [BoundingBox(x, y, x_plus_width - x, y_plus_height - y, "person")
                for (x, y, x_plus_width, y_plus_height) in final_detected_people]


class CarDetector(Detector):
    """
    Detects cars within a frame
    """

    def __init__(self, car_cascade_src: str):
        """
        Initialises the class using the provided car cascade required for the detection model

        Model uses a pre-made car cascade model and uses the open_cv CascadeClassifier

        :param car_cascade_src: the file location of the car cascade xml file
        """

        self.car_detector = cv2.CascadeClassifier(car_cascade_src)

    def detect(self, frame: ndarray) -> List[BoundingBox]:
        """
        Detects cars within a frame and returns a list of locations they are detected

        :param frame: the frame to detect the car within
        :return: the list of locations the cars were found to occur
        """

        rectangles = self.car_detector.detectMultiScale(frame, scaleFactor=1.1, minNeighbors=4)

        initial_cars = np.array([[x, y, x + w, y + h] for (x, y, w, h) in rectangles])
        final_cars = non_max_suppression(initial_cars, probs=None, overlapThresh=0.1)

        return [BoundingBox(x, y, x_plus_width - x, y_plus_height - y, "car")
                for (x, y, x_plus_width, y_plus_height) in final_cars]
