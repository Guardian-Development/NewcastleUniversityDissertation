# Activity Analysis Service
This service provides a basis for performing activity anlysis on detected and tracked objects within a video stream. It connects to Apache Kafka, and is then able to detect behaviours, publishing the results back to Apache Kafka. It makes use of Apache Flink to provide scalable analysis.

Project built from template engine found: https://ci.apache.org/projects/flink/flink-docs-release-1.4/quickstart/scala_api_quickstart.html 

## Environment 
- Linux Ubuntu 16.04.3 LTS environment required 

## Install Instructions
- Take base image and install: https://www.ubuntu.com/download/desktop

1. Update Environment 
    - sudo apt-get update 
    - sudo apt-get upgrade 
2. Java 8 Install 
    - sudo apt-get install default-jdk
3. Install SBT
    - sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 2EE0EA64E40A89B84B2DF73499E82A75642AC823
    - sudo apt-get update
    - sudo apt-get install sbt
4. Instal Flink without Hadoop 
    - http://mirror.ox.ac.uk/sites/rsync.apache.org/flink/flink-1.4.1/flink-1.4.1-bin-scala_2.11.tgz
5. tar -xzf flink-1.4.1-bin-scala_2.11.tgz
6. Start a local Flink cluster
    - ./bin/start-local.sh

To stop the Flink cluster: 
- ./bin/stop-local.sh

## To Run
### Command Line
From within the activityanalysisservice folder run: 
- sbt clean run

### IntelliJ
- Edit Configurations -> Add Configuration -> sbt Task 
- Then enter into Tasks: clean run
