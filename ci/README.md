# Setting Up Continour Integration
- Jenkins: Contains the Jenkins Docker image to run the instance of Jenkins. 
- JenkinsData: Contains the persistent storage Docker image for the Jenkins Docker image to use. 

## Steps to setup Jenkins in Docker
1. docker build -t jenkins-data JenkinsData/.
2. docker run --name=jenkins-data jenkins-data
3. docker build -t jenkins Jenkins/.
4. docker run -p 8080:8080 --name=jenkins --volumes-from=jenkins-data -d jenkins
5. To get initial login password run: docker exec jenkins tail -f /var/jenkins_home/secrets/initialAdminPassword 
6. configure sbt with the version you need. 
    - Manage Jenkins -> Global Tool Configuration -> Add Sbt -> Select version for this project.
7. restart the container. 
    - docker stop jenkins
    - docker start jenkins

## Steps to remove Jenkins in Docker
1. docker rm jenkins-data
2. docker stop jenkins
3. docker rm jenkins 
4. docker rmi jenkins-data
5. docker rmi jenkins 


TODO:
- work out how to configure sbt plugin in docker file 
- change jenkins file to build client service and set that to the sbt directory. 


