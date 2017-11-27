# Video Processing Service 
This service provides video streaming and processing. Its goal is to extract enough useful features from a video clip that accurate anomaly detection can be performed on the video stream. The purpose of this service is to turn the video into a series of events, such as people in the video and their position, which should then be sent to server for advanced anomaly detection.

# Requirements
- Docker installed 

# How To Use
#### Running
- docker-compose up (--build)(for first time setup)
#### Stopping
- docker-compose stop
#### Removing
- docker-compose down
#### Viewing running images
- docker-compose ps
