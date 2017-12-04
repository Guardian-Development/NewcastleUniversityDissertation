#!/bin/bash
# Script to setup a ubuntu 16.04 dev environment for the VideoProcessingService

# Update system
sudo apt-get -y update
sudo apt-get -y upgrade
sudo apt-get -y dist-upgrade
sudo apt-get -y autoremove

# Install pip
sudo apt-get install -y python3-pip

# Dependencies for face_recognition package
apt-get install -y --fix-missing \
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
    python3.4-dev \
    python3.4-numpy \
    software-properties-common \
    zip \
    && apt-get clean && rm -rf /tmp/* /var/tmp/*

cd ~ && \
    mkdir -p dlib && \
    git clone -b 'v19.7' --single-branch https://github.com/davisking/dlib.git dlib/ && \
    cd  dlib/ && \
    python3 setup.py install --yes USE_AVX_INSTRUCTIONS

# Dependencies for OpenCV

cd ~

sudo apt-get autoremove \
    libopencv-dev \
    python-opencv

# Build tools:
sudo apt-get install -y \
    build-essential \
    cmake \
    git \
    pkg-config \
    unzip \
    wget

# GUI
sudo apt-get install -y \
    qt5-default \
    libvtk6-dev

# Media I/O:
sudo apt-get install -y \
    zlib1g-dev \
    libjpeg-dev \
    libwebp-dev \
    libpng-dev \
    libtiff5-dev \
    libjasper-dev \
    libopenexr-dev \
    libgdal-dev \
    libpng12-dev

# Video I/O:
sudo apt-get install -y \
    libdc1394-22-dev \
    libavcodec-dev \
    libavformat-dev \
    libswscale-dev \
    libtheora-dev \
    libvorbis-dev \
    libxvidcore-dev \
    libx264-dev \
    yasm \
    libopencore-amrnb-dev \
    libopencore-amrwb-dev \
    libv4l-dev \
    libxine2-dev \
    libav-tools \
    ffmpeg

# Parallelism and linear algebra libraries:
sudo apt-get install -y libtbb-dev libeigen3-dev

# Python Requirements:
sudo apt-get install -y \
    python-dev \
    python-tk \
    python-numpy \
    python3-dev \
    python3-tk \
    python3-numpy \
    python3-pip

sudo -H pip3 install -U pip numpy
sudo pip3 install virtualenv virtualenvwrapper
echo '# Virtual Environment Wrapper' >> ~/.bashrc
echo 'source /usr/local/bin/virtualenvwrapper.sh' >> ~/.bashrc
mkvirtualenv video-processing-py3 -p python3 

# test virtual environment
workon video-processing-py3
deactivate

# Install OpenCV contrib modules
wget https://github.com/opencv/opencv_contrib/archive/3.3.0.zip
unzip 3.3.0.zip
rm 3.3.0.zip
mv opencv_contrib-3.3.0 opencv_contrib

# Install OpenCV
wget https://github.com/opencv/opencv/archive/3.3.0.zip
unzip 3.3.0.zip
rm 3.3.0.zip
mv opencv-3.3.0 OpenCV
cd OpenCV
mkdir build
cd build
cmake -DCMAKE_BUILD_TYPE=RELEASE \
    -DCMAKE_INSTALL_PREFIX=/usr/local \
    -DOPENCV_EXTRA_MODULES_PATH=~/opencv_contrib/modules \
    -DPYTHON_DEFAULT_EXECUTABLE=$(which python3) \
    -DWITH_QT=ON \
    -DWITH_OPENGL=ON \
    -DFORCE_VTK=ON \
    -DWITH_TBB=ON \
    -DWITH_GDAL=ON \
    -DWITH_XINE=ON \
    -DBUILD_EXAMPLES=OFF \
    -DENABLE_PRECOMPILED_HEADERS=OFF ..
make -j4
sudo make install
sudo ldconfig

cd ~/.virtualenvs/video-processing-py3/lib/python3.5/site-packages
ln -s /usr/local/lib/python3.5/dist-packages/cv2.cpython-35m-x86_64-linux-gnu.so cv2.so

