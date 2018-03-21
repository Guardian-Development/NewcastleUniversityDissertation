# Data Storage Service

This service provides a basis for storing the data within Apache Kafka to Neo4j, to allow querying and visualisation of data.

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
    - echo "deb https://dl.bintray.com/sbt/debian /" | sudo tee -a /etc/apt/sources.list.d/sbt.list 
    - sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 2EE0EA64E40A89B84B2DF73499E82A75642AC823
    - sudo apt-get update
    - sudo apt-get install sbt
4. Instal Flink without Hadoop 
    - http://mirror.ox.ac.uk/sites/rsync.apache.org/flink/flink-1.4.1/flink-1.4.1-bin-scala_2.11.tgz
5. tar -xzf flink-1.4.1-bin-scala_2.11.tgz
6. Start a local Flink cluster
    - ./bin/start-local.sh
7. Download Neo4j
    - https://neo4j.com/download/
8. chmod +x {IMAGE NAME}
8. run the Neo4j image downloaded

To stop the Flink cluster: 
- ./bin/stop-local.sh

## To Run
### Command Line
From within the activityanalysisservice folder run: 
- sbt clean run

### IntelliJ
- Edit Configurations -> Add Configuration -> sbt Task 
- Then enter into Tasks: clean run

## Running in Flink Cluster
1. Build project into fat jar
    - sbt clean assembly (from within project folder)
2. Submit job to flink: 
    - flink-1.4.1/bin/flink run path-to-fat-jar (command line options)
3. To see job running visit: 
    - localhost:8081

To view stdout from the flink task running:
- tail -f flink-1.4.1/log/flink*-taskmanager-*.out

