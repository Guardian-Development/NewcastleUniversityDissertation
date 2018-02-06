"""
Provides a way of defining custom stopping criteria's for video processing
"""
import cv2


class VideoProcessingStoppingCriteria:
    """
    Provides the ability to define custom stopping criteria's
    """

    def should_stop_processing(self) -> bool:
        """
        Allows a boolean condition check to be made

        :return: true if should stop, else false
        """
        raise NotImplementedError


class QuitButtonPressedStoppingCriteria(VideoProcessingStoppingCriteria):
    """
    Provides a stopping criteria of the open_cv 'q' button being clicked
    """

    def should_stop_processing(self) -> bool:
        if cv2.waitKey(1) & 0xFF == ord('q'):
            return True
        return False
