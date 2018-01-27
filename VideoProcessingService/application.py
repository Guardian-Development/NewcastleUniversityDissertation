"""Runs the application

Runs the application
"""
import cv2
from imutils import resize
from video_input.video_input import VideoInputSource
from video_processing.detector import PersonDetector, CarDetector

def start_application(video_source: VideoInputSource):
    """Runs the application
    
    Starts the application
    """
    video_source.open_source()
    person_detector = PersonDetector()
    car_detector = CarDetector(car_cascade_src="video_processing/detection_models/car_cascade.xml")

    while video_source.source_open():

        got_next_frame, frame = video_source.get_next_frame()

        if not got_next_frame:
            break

        frame_processed = resize(frame, width=400, height=400)
        frame_processed = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

        #TODO: create proper decoupled pipelining
        people_locations = person_detector.detect(frame_processed)
        car_locations = car_detector.detect(frame_processed)

       	for x, y, x_plus_width, y_plus_height in people_locations:
            cv2.rectangle(frame, (x, y), (x_plus_width, y_plus_height), (0, 255, 0), 2)
        
        for x, y, x_plus_width, y_plus_height in car_locations:
            cv2.rectangle(frame, (x, y), (x_plus_width, y_plus_height), (0, 0, 255), 2)

        cv2.imshow('Video', frame)
        if cv2.waitKey(1) & 0xFF == ord('q'):
            break

    video_source.close_source()
    cv2.destroyAllWindows()
