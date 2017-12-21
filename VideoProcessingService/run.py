"""Runs the application

Runs the application
"""

import cv2
from video_input.video_input import WebCamVideoInputSource, LocalFileVideoSource
from video_processing.facial_recognition import FacialRecogniser

def main():
    """Runs the application
    
    Starts the application
    """
    video_source = WebCamVideoInputSource(-1)
    video_source.open_source()

    face_detector = FacialRecogniser()

    while video_source.source_open():

        _, frame = video_source.get_next_frame()
        face_locations = face_detector.detect_face_locations(frame)

        for(top, right, bottom, left) in face_locations:
            cv2.rectangle(frame, (left, top), (right, bottom), (0, 0, 255), 2)
            cv2.rectangle(frame, (left, bottom - 35), (right, bottom), (0, 0, 255), cv2.FILLED)
            font = cv2.FONT_HERSHEY_DUPLEX
            cv2.putText(frame, 'Face', (left + 6, bottom - 6), font, 1.0, (255, 255, 255), 1)

        cv2.imshow('Video', frame)
        if cv2.waitKey(1) & 0xFF == ord('q'):
            break

    video_source.close_source()
    cv2.destroyAllWindows()

if __name__ == '__main__':
    main()
