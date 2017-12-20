"""Runs the application

Runs the application
"""

import face_recognition
import cv2
from video_input.video_input import WebCamVideoInputSource, LocalFileVideoSource

def main():
    """Runs the application
    
    Starts the application
    """
    obama_image = face_recognition.load_image_file("obama.jpg")
    obama_face_encoding = face_recognition.face_encodings(obama_image)[0]

    video_source = WebCamVideoInputSource(-1)
    video_source.open_source()

    while video_source.source_open():

        _, frame = video_source.get_next_frame()
        face_locations = face_recognition.face_locations(frame)
        face_encodings = face_recognition.face_encodings(frame, face_locations)

        for(top, right, bottom, left), face_encoding in zip(face_locations, face_encodings):

            match = face_recognition.compare_faces([obama_face_encoding], face_encoding)

            name = "unknown"
            if match[0]: 
                name = "Obama"
        
            cv2.rectangle(frame, (left, top), (right, bottom), (0, 0, 255), 2)
            cv2.rectangle(frame, (left, bottom - 35), (right, bottom), (0, 0, 255), cv2.FILLED)
            font = cv2.FONT_HERSHEY_DUPLEX
            cv2.putText(frame, name, (left + 6, bottom - 6), font, 1.0, (255, 255, 255), 1)

        cv2.imshow('Video', frame)
    
        if cv2.waitKey(1) & 0xFF == ord('q'):
            break 

    video_source.close_source()
    cv2.destroyAllWindows()

if __name__ == '__main__':
    main()
