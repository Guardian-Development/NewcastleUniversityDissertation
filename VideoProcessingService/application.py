"""Runs the application

Runs the application
"""
import cv2
from video_input.video_input import VideoInputSource
from video_processing.detector import PersonDetector

def start_application(video_source: VideoInputSource):
    """Runs the application
    
    Starts the application
    """
    video_source.open_source()
    person_detector = PersonDetector()

    while video_source.source_open():

        got_next_frame, frame = video_source.get_next_frame()

        if not got_next_frame:
            continue

        face_locations = person_detector.detect(frame)

        for(top, right, bottom, left) in face_locations:
            cv2.rectangle(frame, (left, top), (right, bottom), (0, 0, 255), 2)
            cv2.rectangle(frame, (left, bottom - 35), (right, bottom), (0, 0, 255), cv2.FILLED)
            font = cv2.FONT_HERSHEY_DUPLEX
            cv2.putText(frame, 'Person', (left + 6, bottom - 6), font, 1.0, (255, 255, 255), 1)

        cv2.imshow('Video', frame)
        if cv2.waitKey(1) & 0xFF == ord('q'):
            break

    video_source.close_source()
    cv2.destroyAllWindows()
