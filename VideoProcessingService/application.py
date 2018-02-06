"""
Runs the application
"""
import cv2
from video_pipeline.pipeline import VideoPipeline


def start_application(video_pipeline: VideoPipeline) -> None:
    """
    Runs the application

    :param video_pipeline: the video pipeline to run
    :return: None
    """
    video_pipeline.run_pipeline()
    cv2.destroyAllWindows()
