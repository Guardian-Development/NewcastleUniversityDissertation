import face_recognition
import cv2

#obama_image = face_recognition.load_image_file("obama.jpg")
#obama_face_encoding = face_recognition.face_encodings(obama_image)[0]

print('Hello World')
print(face_recognition.__version__)
print(cv2.__version__)
print(cv2.__file__)
print(cv2.getBuildInformation())

# video_capture = cv2.VideoCapture(0)

# while True: 

#     returned, frame = video_capture.read() 
#     print(returned)
#     print(frame)
    
#     if cv2.waitKey(1) & 0xFF == ord('q'):
#         break 

# video_capture.release()
# cv2.destroyAllWindows()