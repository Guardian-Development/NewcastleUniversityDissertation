# Anomaly Detection Service

This service provides a basis for performing anomaly detection on behaviours and objects detected within a video stream. It connects to Apache Kafka, and is then able to detect anomalies, publishing the results back to Apache Kafka. It makes use of Apache Spark to provide scalable detections.

Project built from template engine found: https://github.com/holdenk/sparkProjectTemplate.g8

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
    - echo "deb https://dl.bintray.com/sbt/debian /" | sudo tee -a /etc/apt/sources.list.d/sbt.list 
    - sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 2EE0EA64E40A89B84B2DF73499E82A75642AC823
    - sudo apt-get update
    - sudo apt-get install sbt
4. Install Apache Spark without Hadoop: 
    - https://www.apache.org/dyn/closer.lua/spark/spark-2.3.0/spark-2.3.0-bin-without-hadoop.tgz 
5. tar -xzf spark-2.3.0-bin-without-hadoop.tgz

## To Run
### Command Line
From within the activityanalysisservice folder run: 
- sbt clean run

### IntelliJ
- Edit Configurations -> Add Configuration -> sbt Task 
- Then enter into Tasks: clean run