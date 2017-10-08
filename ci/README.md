# Setting Up Continour Integration

## Steps to setup Jenkins in Docker
1. docker build -t jenkins-data JenkinsData/.
2. docker run --name=jenkins-data jenkins-data
3. docker build -t jenkins Jenkins/.
4. docker run -p 8080:8080 --name=jenkins --volumes-from=jenkins-data -d jenkins

This, at present, gives a blank Jenkins instance with persisted storage locally. Next steps will be working out what I require on the image to run scala builds and docker and then create the neccessary tasks to set up CI. 

