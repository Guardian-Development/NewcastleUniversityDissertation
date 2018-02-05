"""Entry point for the application to start

Responsible for starting the application correctly based on the command line arguments passed
"""
from command_line_procesor import process_command_line_arguments, VideoProcessingType
from application import start_application
from video_pipeline.pipeline import VideoPipelineBuilder
from video_input.video_input import WebCamVideoInputSource, LocalFileVideoSource
from video_processing.frame_processing import ResizeFrameProcessor, BlackAndWhiteFrameProcessor
from video_processing.detector import CarDetector, PersonDetector
from video_processing.tracker import Tracker
from video_processing.stopping_criteria import QuitButtonPressedStoppingCriteria
from video_output.video_output import LocalDisplayVideoOutput
from message_sending.sender import ApacheKafkaMessageSender

if __name__ == '__main__':
    """Parses command line input, and starts the video processing pipeline
    """

    ARGUMENTS = process_command_line_arguments()

    PIPELINE = None
    if ARGUMENTS.videosource == VideoProcessingType.webcam:
        PIPELINE = VideoPipelineBuilder(WebCamVideoInputSource(ARGUMENTS.webcam))
    else:
        PIPELINE = VideoPipelineBuilder(LocalFileVideoSource(ARGUMENTS.filelocation))

    PIPELINE = \
        PIPELINE.with_frame_processing_stage(ResizeFrameProcessor(width=500, height=500))\
                .with_video_processing_stage(Tracker([
                        PersonDetector()
                    ]))\
                .with_video_output_stage(LocalDisplayVideoOutput())\
                .with_processing_stopping_criteria(QuitButtonPressedStoppingCriteria())
               
    if ARGUMENTS.kafkaurl is not None and ARGUMENTS.kafkatopic is not None:
        PIPELINE = PIPELINE.with_message_sender(ApacheKafkaMessageSender(
            server_address=ARGUMENTS.kafkaurl, topic=ARGUMENTS.kafkatopic))

    start_application(PIPELINE.build())
