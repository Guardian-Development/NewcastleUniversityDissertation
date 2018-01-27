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

        people_locations = person_detector.detect(frame)

       	for x, y, x_plus_width, y_plus_height in people_locations:
            cv2.rectangle(frame, (x, y), (x_plus_width, y_plus_height), (0, 255, 0), 2)

        cv2.imshow('Video', frame)
        if cv2.waitKey(1) & 0xFF == ord('q'):
            break

    video_source.close_source()
    cv2.destroyAllWindows()
