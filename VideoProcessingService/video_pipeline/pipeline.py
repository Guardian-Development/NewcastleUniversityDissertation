"""
Provides a pipeline for executing video processing
Has a single video input source, multiple processing stages, and a single stopping criteria
"""
from typing import List
from video_input.video_input import VideoInputSource
from video_processing.frame_processing import FrameProcessor
from video_processing.detector import Detector
from video_processing.stopping_criteria import VideoProcessingStoppingCriteria
from video_output.video_output import VideoOutput
from message_sending.sender import MessageSender


class VideoPipeline(object):
    """
    The video processing pipeline
    Allows execution of a data pipeline for video processing
    """

    def __init__(self,
                 video_input: VideoInputSource,
                 video_frame_processing_stages: List[FrameProcessor],
                 video_processing_stages: List[Detector],
                 video_output_stages: List[VideoOutput],
                 message_senders: List[MessageSender],
                 video_processing_stopping_criteria: VideoProcessingStoppingCriteria):
        """
        Initialises the pipeline with the passed in stages

        :param video_input: the source of the video pipeline
        :param video_frame_processing_stages: the transforms/filters that should be applied to each frame
        :param video_processing_stages: the detectors that should be applied to each frame to detect objects
        :param video_output_stages: the output locations of the pipeline
        :param message_senders: senders responsible for storing object locations detected in each frame
        :param video_processing_stopping_criteria: enable early termination of the pipeline
        """
        self.video_input = video_input
        self.video_frame_processing_stages = video_frame_processing_stages
        self.video_processing_stages = video_processing_stages
        self.video_output_stages = video_output_stages
        self.message_senders = message_senders
        self.video_processing_stopping_criteria = video_processing_stopping_criteria

    def run_pipeline(self) -> None:
        """
        Runs the pipeline for the given stages passed in to the constructor

        - Gets the frame from the source
        - Applies each frame processing stage in the order in which they were passed in
        - Detects objects using video processing stages in the order in which they were passed in
        - Outputs the detections and the frame to each of the output stages in the order they were passed in
        - Checks for stopping criteria met, if so completes the pipeline and returns

        :return: None
        """
        self.video_input.open_source()

        while self.video_input.source_open():

            got_next_frame, frame = self.video_input.get_next_frame()
            if not got_next_frame:
                break

            processed_frame = frame
            for video_frame_processing_stage in self.video_frame_processing_stages:
                processed_frame = video_frame_processing_stage.process_frame(processed_frame)

            object_locations = []
            for video_processing_stage in self.video_processing_stages:
                object_locations.extend(video_processing_stage.detect(processed_frame))

            for video_output_stage in self.video_output_stages:
                video_output_stage.produce_output(processed_frame, object_locations)

            for message_sender in self.message_senders:
                message_sender.send_message(object_locations)

            if self.video_processing_stopping_criteria.should_stop_processing():
                break

        self.video_input.close_source()


class VideoPipelineBuilder(object):
    """
    Allows easy building of a video processing pipeline
    """

    def __init__(self, video_input: VideoInputSource):
        """
        Initialises the builder with the given required video source

        :param video_input: the source of the video strea
        """
        self.video_input = video_input
        self.video_frame_processing_stages = []
        self.video_processing_stages = []
        self.video_output_stages = []
        self.message_senders = []
        self.video_processing_stopping_criteria = None

    def with_frame_processing_stage(self, video_frame_processing_stage: FrameProcessor) -> "VideoPipelineBuilder":
        """
        Adds a frame processing stage to the pipeline

        :param video_frame_processing_stage: the frame processing stage to add to the pipeline
        :return: self
        """
        self.video_frame_processing_stages.append(video_frame_processing_stage)
        return self

    def with_video_processing_stage(self, video_processing_stage: Detector) -> "VideoPipelineBuilder":
        """
        Adds a video processing stage to the pipeline

        :param video_processing_stage: the detector stage to add to the pipeline
        :return: self
        """
        self.video_processing_stages.append(video_processing_stage)
        return self

    def with_video_output_stage(self, video_output_stage: VideoOutput) -> "VideoPipelineBuilder":
        """
        Adds a video output stage to the pipeline
        :param video_output_stage: the video output stage to add to the pipeline
        :return: self
        """
        self.video_output_stages.append(video_output_stage)
        return self

    def with_message_sender(self, message_sender: MessageSender) -> "VideoPipelineBuilder":
        """
        Adds a message sender to the pipeline

        :param message_sender: the message sender to add to the pipeline
        :return: self
        """
        self.message_senders.append(message_sender)
        return self

    def with_processing_stopping_criteria(
            self,
            processing_stopping_criteria: VideoProcessingStoppingCriteria) -> "VideoPipelineBuilder":
        """
        Adds the early stopping criteria to the pipeline

        :param processing_stopping_criteria: the pipeline early stopping criteria stage
        :return: self
        """
        self.video_processing_stopping_criteria = processing_stopping_criteria
        return self

    def build(self) -> VideoPipeline:
        """
        Builds the pipeline for the given parameters

        :return: the final video processing pipeline
        """
        return VideoPipeline(self.video_input,
                             self.video_frame_processing_stages,
                             self.video_processing_stages,
                             self.video_output_stages,
                             self.message_senders,
                             self.video_processing_stopping_criteria)
