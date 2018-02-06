"""
Provides frame by frame video reading capabilities
"""
from typing import Tuple
from cv2 import VideoCapture
from numpy import ndarray


class VideoInputSource:
    """
    Represents a video input that can be read
    """

    def open_source(self) -> bool:
        """
        Opens the video source so that other functions become available on it
        :return: true if was able to open the source, else false
        """
        raise NotImplementedError

    def close_source(self) -> None:
        """
        Closes the source of this video input
        :return: None
        """
        raise NotImplementedError

    def get_next_frame(self) -> Tuple[bool, ndarray]:
        """
        Gets the next frame in the video input source
        :return: true if was able to get the next frame, and the next frame, else false and None
        """
        raise NotImplementedError

    def source_open(self) -> bool:
        """
        Allows caller to know whether this video source is currently open or not
        :return: true if video source is open, else false
        """
        raise NotImplementedError


class WebCamVideoInputSource(VideoInputSource):
    """
    Allows for live reading from a Web cam attached to this device
    """

    def __init__(self, webcam_source: int) -> None:
        """
        Initialises the video input source to use the webcam with the passed ID

        :param webcam_source: the webcam ID, use -1 to use default
        """
        self.webcam_source = webcam_source
        self.video_capture = None

    def open_source(self) -> bool:
        self.video_capture = VideoCapture(self.webcam_source)
        return True

    def close_source(self) -> None:
        self.video_capture.release()

    def source_open(self) -> bool:
        return self.video_capture.isOpened()

    def get_next_frame(self) -> Tuple[bool, ndarray]:
        return self.video_capture.read()


class LocalFileVideoSource(VideoInputSource):
    """
    Allows for reading from a local video file
    """

    def __init__(self, file_path: str) -> None:
        """
        Initialises the local file video source to use the file_path specified
        :param file_path: path to local video file
        """
        self.video_file_path = file_path
        self.video_capture = None

    def open_source(self) -> bool:
        self.video_capture = VideoCapture(self.video_file_path)
        return True

    def close_source(self) -> None:
        self.video_capture.release()

    def source_open(self) -> bool:
        return self.video_capture.isOpened()

    def get_next_frame(self) -> Tuple[bool, ndarray]:
        return self.video_capture.read()
