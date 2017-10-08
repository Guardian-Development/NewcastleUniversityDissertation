# Setting Up Continour Integration
- Jenkins: Contains the Jenkins Docker image to run the instance of Jenkins. 
- JenkinsData: Contains the persistent storage Docker image for the Jenkins Docker image to use. 

## Steps to setup Jenkins in Docker
1. docker build -t jenkins-data JenkinsData/.
2. docker run --name=jenkins-data jenkins-data
3. docker build -t jenkins Jenkins/.
4. docker run -p 8080:8080 --name=jenkins --volumes-from=jenkins-data -d jenkins

This, at present, gives a blank Jenkins instance with persisted storage locally. Next steps will be working out what I require on the image to run scala builds and docker and then create the neccessary tasks to set up CI. 

