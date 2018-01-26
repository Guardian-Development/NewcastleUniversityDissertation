"""Entry point for the application to start

Responsible for starting the application correctly based on the command line arguments passed
"""
from command_line_procesor import process_command_line_arguments, VideoProcesingType
from video_input.video_input import WebCamVideoInputSource, LocalFileVideoSource
from application import start_application

if __name__ == '__main__':
    ARGUMENTS = process_command_line_arguments()

    if ARGUMENTS.videosource == VideoProcesingType.webcam:
        print(ARGUMENTS.videosource)
        print(ARGUMENTS.webcam)
        start_application(WebCamVideoInputSource(ARGUMENTS.webcam))
    else:
        print(ARGUMENTS.videosource)
        print(ARGUMENTS.filelocation)
        start_application(LocalFileVideoSource(ARGUMENTS.filelocation))
