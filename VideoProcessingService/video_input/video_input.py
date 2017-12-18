from cv2 import VideoCapture 
from numpy import ndarray
from typing import Tuple

class VideoInputSource:
    """Represents a video input that can be read
    
    Gains access to local video source and allows frame by frame reading
    """
    
    def openSource(self) -> bool:
        """Opens the video source to make it available
        
        Opens the video source so that other functions become available on it
        
        Raises:
            NotImplementedError -- [description]
        """
        
        raise NotImplementedError
    
    def closeSource(self) -> None:
        """Closes the source of this video input 
        
        closes the sourse of this video input
        
        """
        
        raise NotImplementedError

    def getNextFrame(self) -> Tuple[bool, ndarray]:
        """Gets the next frame in the video input source

        Returns the next frame of the video source along with a 
        flag that shows whether it was able to read the next frame
        
        Raises:
            NotImplementedError -- should be implemented in child classes
        """
        
        raise NotImplementedError
    
    def sourceOpen(self) -> bool:
        """Returns true if the video source is currently open
        
        Returns true if the source is open, else false 
        
        Raises:
            NotImplementedError -- should be implemented in child classes
        """
        
        raise NotImplementedError

class WebCamVideoInputSource(VideoInputSource):
    """Allows for live reading from a Web cam attathed to this device 
    
    Makes use of opencv to read frames from an attached webcam
    """
    

    def __init__(self, webcam_source: int) -> None:
        """Initialises the video input source to use the webcam with the passed ID
        
        Initialises the webcam source making use of opencv
        
        Arguments:
            webcam_source: int {int} -- the webcam ID, use -1 to use default 
        """
        
        self.webcam_source = webcam_source

    def openSource(self) -> bool:
        """Opens the connection to the webcam to allow reading
        
        Makes use of opencv to read from the webcam source
        
        Returns:
            [bool] -- [true if successfully opens the source, else false]
        """
        

        self.video_capture = VideoCapture(self.webcam_source)
        return True

    def closeSource(self) -> None:
        """Closes the connection to the webcam 
        
        Releases opencv connection to webcam 
        """
        
        self.video_capture.release()
    
    def sourceOpen(self) -> bool:
        """Returns whether opencv is currently reading from webcam
        
        Makes use of opencv to test whether we are accessing the webcam or not
        
        Returns:
            [bool] -- [returns true if we are accessing the webcam, else false]
        """
        
        return self.video_capture.isOpened() 
    
    def getNextFrame(self) -> Tuple[bool, ndarray]: 
        """Gets the next frame from the webcam
        
        Uses opencv to get the next available frame from the webcam source
        
        Returns:
            [bool, ndarray] -- [returns true and the frame if next frame available, else false and empty array]
        """
        
        return self.video_capture.read()


