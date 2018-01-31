"""Entry point for the application to start

Responsible for starting the application correctly based on the command line arguments passed
"""
from command_line_procesor import process_command_line_arguments, VideoProcesingType
from application import start_application
from video_pipeline.pipeline import VideoPipelineBuilder
from video_input.video_input import WebCamVideoInputSource, LocalFileVideoSource
from video_processing.frame_processing import ResizeFrameProcessor, BlackAndWhiteFrameProcessor
from video_processing.detector import CarDetector, PersonDetector
from video_processing.stopping_criteria import QuitButtonPressedStoppingCriteria
from video_output.video_output import LocalDisplayVideoOutput
from message_sending.sender import ApacheKafkaMessageSender

if __name__ == '__main__':
    ARGUMENTS = process_command_line_arguments()

    PIPELINE = None
    if ARGUMENTS.videosource == VideoProcesingType.webcam:
        PIPELINE = VideoPipelineBuilder(WebCamVideoInputSource(ARGUMENTS.webcam))
    else:
        PIPELINE = VideoPipelineBuilder(LocalFileVideoSource(ARGUMENTS.filelocation))

    PIPELINE = \
        PIPELINE.with_frame_processing_stage(ResizeFrameProcessor(width=400, height=400))\
                .with_frame_processing_stage(BlackAndWhiteFrameProcessor())\
                .with_video_processing_stage(CarDetector(
                    car_cascade_src="video_processing/detection_models/car_cascade.xml"))\
                .with_video_processing_stage(PersonDetector())\
                .with_video_output_stage(LocalDisplayVideoOutput())\
                .with_processing_stopping_criteria(QuitButtonPressedStoppingCriteria())\
                .build()

    # Test of ability to send message to Apache Kafka
    TEST_KAFKA_SENDER = ApacheKafkaMessageSender(
        server_address="localhost", topic="video_processing")
    TEST_KAFKA_SENDER.send_message("test message")

    start_application(PIPELINE)
