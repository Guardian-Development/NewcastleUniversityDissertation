"""Provides facial recognition capabilities over images stored as numpy arrays

Makes use of the face_recognition library for facial recognition capabilities
"""

from typing import List, Tuple
from numpy import ndarray
import face_recognition

class FacialRecogniser(object):
    """Provides facial recognition capabilities
    
    Allows face recognition over ndarray frames using the face_recognition library
    """

    def detect_face_locations(self, frame: ndarray) -> List[Tuple[float, float, float, float]]:
        """Detects all the faces it can see in the provided frame
        
        Uses the face_recognition library to detect the locations of all the faces in the frame
        
        Arguments:
            frame: ndarray {[ndarray]} -- [the frame to detect faces in]
        
        Returns:
            [List[Tuple[float, float, float, float]]] -- [the (top, bottom, left, right) location of each face]
        """
        
        return face_recognition.face_locations(frame)
