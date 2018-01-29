# Video Processing Service

This service provides video annotation and object tracking. Given a video feed, it will detect various objects within the feed and then has the ability to send this information to a server which can then perform event extrapolation and anomaly detection.

## Environment

- Linux Ubuntu 16.04.3 LTS environment required

### Install Instructions

- Take base image and install: https://www.ubuntu.com/download/desktop

1. Update Environment
    - sudo apt-get update
    - sudo apt-get upgrade

2. OpenCV required packages
    - sudo apt-get install build-essential cmake git pkg-config
    - sudo apt-get install libjpeg8-dev libtiff5-dev libjasper-dev libpng12-dev
    - sudo apt-get install libavcodec-dev libavformat-dev libswscale-dev libv4l-dev
    - sudo apt-get install libgtk2.0-dev
    - sudo apt-get install libatlas-base-dev gfortran

3. Python install
    - wget https://bootstrap.pypa.io/get-pip.py
    - sudo python3 get-pip.py
    - sudo pip3 install virtualenv virtualenvwrapper

4. Add to ~/.bashrc
    - export VIRTUALENVWRAPPER_PYTHON=/usr/bin/python3
    - export WORKON_HOME=$HOME/.virtualenvs
    - source /usr/local/bin/virtualenvwrapper.sh

5. Reload ~/.bashrc and create virtual env named cv
    - source ~/.bashrc
    - mkvirtualenv cv

6. To install packages and run
    - workon cv
    - pip install -r requirements.txt (from VideoProcessingService directory)
    - run: python run.py -h

### Test Videos

    - https://www.youtube.com/watch?v=hTUyzF4v9KA
    - https://www.youtube.com/watch?v=YwbP3Z30gUY
    - https://www.youtube.com/watch?v=Y1jTEyb3wiI