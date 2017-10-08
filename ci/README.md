# Setting Up Continour Integration

## Steps to setup Jenkins in Docker 
1. In current directory run: docker build -t guardian-jenkins .
2. If you already had the image running: 
    1. docker stop guardian-jenkins
    2. docker rm guardian-jenkins
3. To run the image: docker run -p 8080:8080 --name=guardian-jenkins -d guardian-jenkins

This will run the image as a demon process, and logs are sent to a specified folder in the DockerImage for Jenkins. 
