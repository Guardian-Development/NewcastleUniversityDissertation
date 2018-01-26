# Video Processing Service 
This service provides video streaming and processing. Its goal is to extract enough useful features from a video clip that accurate anomaly detection can be performed on the video stream. The purpose of this service is to turn the video into a series of events, such as people in the video and their position, which should then be sent to server for advanced anomaly detection.

# Environment 
- Linux Ubuntu 16.04.3 LTS environment required 

# To setup:
- Take base image and install: https://www.ubuntu.com/download/desktop 
Install Instruction Opencv:

## Install Instructions 

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

6. Install required packages for Python
    - sudo apt-get install python3-dev
    - sudo rm -rf ~/.cache/pip/
    - pip install numpy

7. Get OpenCV and the contrib modules
    - cd ~
    - git clone https://github.com/Itseez/opencv.git
    - cd opencv
    - git checkout 3.3.0
    - cd ~
    - git clone https://github.com/Itseez/opencv_contrib.git
    - cd opencv_contrib
    - git checkout 3.3.0

8. Build OpenCV with contib modules
    - cd ~/opencv
    - mkdir build
    - cd build
    - cmake -D CMAKE_BUILD_TYPE=RELEASE \
	-D CMAKE_INSTALL_PREFIX=/usr/local \
	-D INSTALL_C_EXAMPLES=OFF \
	-D INSTALL_PYTHON_EXAMPLES=ON \
	-D OPENCV_EXTRA_MODULES_PATH=~/opencv_contrib/modules \
	-D BUILD_EXAMPLES=ON ..

    - make -j4
    - sudo make install
    - sudo ldconfig

9. Link OpenCV with Python environment
    - ls -l /usr/local/lib/python3.5/site-packages/
    - cd ~/.virtualenvs/cv/lib/python3.5/site-packages/
    - ln -s /usr/local/lib/python3.5/site-packages/cv2.cpython-34m.so cv2.so

10. Install face_reocognition requirements: 
    - apt-get install -y --fix-missing \
        build-essential \
        cmake \
        gfortran \
        git \
        wget \
        curl \
        graphicsmagick \
        libgraphicsmagick1-dev \
        libatlas-dev \
        libavcodec-dev \
        libavformat-dev \
        libboost-all-dev \
        libgtk2.0-dev \
        libjpeg-dev \
        liblapack-dev \
        libswscale-dev \
        pkg-config \
        python3-dev \
        python3-numpy \
        software-properties-common \
        zip \

    - mkdir -p dlib
    - git clone -b 'v19.7' --single-branch https://github.com/davisking/dlib.git dlib/
    - cd  dlib/ 
    - workon cv (virtual environment for python)
    - python3 setup.py install --yes USE_AVX_INSTRUCTIONS