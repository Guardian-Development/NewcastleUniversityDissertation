"""Provides a pipeline for executing video processing

Has a single video input source, multiple processing stages, and a single stopping criteria
"""

from typing import List
from video_input.video_input import VideoInputSource
from video_processing.frame_processing import FrameProcessor
from video_processing.detector import Detector
from video_processing.stopping_criteria import VideoProcessingStoppingCriteria
from video_output.video_output import VideoOutput

class VideoPipeline(object):
    """The video processing pipeline

    Allows execution of a data pipeline for video processing
    """

    def __init__(self,
                 video_input: VideoInputSource,
                 video_frame_processing_stages: List[FrameProcessor],
                 video_processing_stages: List[Detector],
                 video_output_stages: List[VideoOutput],
                 video_processing_stopping_criteria: VideoProcessingStoppingCriteria):
        """Initialises the pipeline with the passed in stages

        Arguments:
            video_input: VideoInputSource {[VideoInputSource]}
                -- [the source of the video pipeline]
            video_frame_processing_stages: List[FrameProcessor] {[FrameProcessor]}
                -- [the transforms/filters that should be applied to each frame]
            video_processing_stages: List[Detector] {[Detector]}
                -- [the detectors that should be applied to each frame to detect objects]
            video_output_stages: List[VideoOutput] {[VideoOutput]}
                -- [the output locations of the pipeline]
            video_processing_stopping_criteria: VideoProcessingStoppingCriteria {[VideoProcessingStoppingCriteria]}
                -- [the stopping criteria that should be met to enable early termination of the pipeline]
        """

        self.video_input = video_input
        self.video_frame_processing_stages = video_frame_processing_stages
        self.video_processing_stages = video_processing_stages
        self.video_output_stages = video_output_stages
        self.video_processing_stopping_criteria = video_processing_stopping_criteria

    def run_pipeline(self):
        """Runs the pipeline for the given stages passed in to the constructor

        - Gets the frame from the source
        - Applies each frame processing stage in the order in which they were passed in
        - Detects objects using video processing stages in the order in which they were passed in
        - Outputs the detections and the frame to each of the output stages in the order they were passed in
        - Checks for stopping criteria met, if so completes the pipeline and returns

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
            
            if self.video_processing_stopping_criteria.should_stop_processing():
                break
        
        self.video_input.close_source()

class VideoPipelineBuilder(object):
    """Allows easy building of a video processing pipeline
    """

    def __init__(self, video_input: VideoInputSource):
        """Initialises the builder with the given compulsary video source
        
        Arguments:
            video_input: VideoInputSource {[VideoInputSource]} -- [the source of the video stream]
        """

        self.video_input = video_input
        self.video_frame_processing_stages = []
        self.video_processing_stages = []
        self.video_output_stages = []
        self.video_processing_stopping_criteria = None

    def with_frame_processing_stage(self, video_frame_processing_stage: FrameProcessor) -> "VideoPipelineBuilder":
        """Adds a frame processing stage to the pipeline
        
        Arguments:
            video_frame_processing_stage: FrameProcessor {[FrameProcessor]}
                -- [the frame processing stage to add to the pipeline]
        
        Returns:
            [VideoPipelineBuilder] -- [returns this]
        """

        self.video_frame_processing_stages.append(video_frame_processing_stage)
        return self

    def with_video_processing_stage(self, video_processing_stage: Detector) -> "VideoPipelineBuilder":
        """Adds a video processing stage to the pipeline
        
        Arguments:
            video_processing_stage: Detector {[Detector]} -- [the detector stage to add to the pipeline]
        
        Returns:
            [VideoPipelineBuilder] -- [returns this]
        """

        self.video_processing_stages.append(video_processing_stage)
        return self

    def with_video_output_stage(self, video_output_stage: VideoOutput) -> "VideoPipelineBuilder":
        """Adds a video output stage to the pipeline
        
        Arguments:
            video_output_stage: VideoOutput {[VideoOutput]} -- [the video output stage to add to the pipeline]
        
        Returns:
            [VideoPipelineBuilder] -- [returns this]
        """

        self.video_output_stages.append(video_output_stage)
        return self

    def with_processing_stopping_criteria(self, processing_stopping_criteria: VideoProcessingStoppingCriteria) -> "VideoPipelineBuilder":
        """Adds the early stopping criteria to the pipeline
        
        Arguments:
            processing_stopping_criteria: VideoProcessingStoppingCriteria {[VideoProcessingStoppingCriteria]}
                -- [the pipeline early stopping criteria stage]
        
        Returns:
            [VideoPipelineBuilder] -- [returns this]
        """

        self.video_processing_stopping_criteria = processing_stopping_criteria
        return self

    def build(self) -> VideoPipeline:
        """Builds the pipeline for the given parameters
        
        Returns:
            [VideoPipeline] -- [the final video processing pipeline]
        """

        return VideoPipeline(self.video_input,
                             self.video_frame_processing_stages,
                             self.video_processing_stages,
                             self.video_output_stages,
                             self.video_processing_stopping_criteria)
