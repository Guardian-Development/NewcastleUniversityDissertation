"""Provides a way of defining custom stopping criterias for video processing
"""
import cv2


class VideoProcessingStoppingCriteria:
    """Provides the ability to define custom stopping criterias
    """

    def should_stop_processing(self) -> bool:
        """Allows a boolean condition check to be made
        
        Raises:
            NotImplementedError -- should be implemented in child classes
        """

        raise NotImplementedError


class QuitButtonPressedStoppingCriteria(VideoProcessingStoppingCriteria):
    """Provides a stopping criteria of the open_cv 'q' button being clicked
    """

    def should_stop_processing(self) -> bool:
        """Provides a stopping criteria of the opencv 'q' button being clicked
        
        Returns:
            [bool] -- [true if q was clicked, else false]
        """

        if cv2.waitKey(1) & 0xFF == ord('q'):
            return True
        return False
