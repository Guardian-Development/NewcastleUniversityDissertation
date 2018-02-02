"""Runs the application

Runs the application
"""
import cv2
from video_pipeline.pipeline import VideoPipeline


def start_application(video_pipeline: VideoPipeline):
    """Runs the application
    
    Starts the application
    """
    video_pipeline.run_pipeline()
    cv2.destroyAllWindows()
