# Support

This project contains the support tools used in the development of the pipeline.

## KafkaLocalStorageTool

Allows for the storing of a Kafka topic to a local json file, making for easy testing and loading of video processing data without needing to re-analyse the video.

## KafkaPublishingTool

Given a JSON file containing a list of messages you wish to publish to Kafka, it published the given messages incrementally to a Kafka topic.