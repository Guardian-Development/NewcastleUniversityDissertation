# Kafka Local Storage Tool

A simple tool that allow the storing of a kafka topic to a local json file.

## Environment 

- Linux Ubuntu 16.04.3 LTS environment required

### Install Instructions

- Take base image and install: https://www.ubuntu.com/download/desktop

1. Update Environment
    - sudo apt-get update
    - sudo apt-get upgrade

2. Python install
    - wget https://bootstrap.pypa.io/get-pip.py
    - sudo python3 get-pip.py
    - sudo pip3 install virtualenv virtualenvwrapper

3. Add to ~/.bashrc
    - export VIRTUALENVWRAPPER_PYTHON=/usr/bin/python3
    - export WORKON_HOME=$HOME/.virtualenvs
    - source /usr/local/bin/virtualenvwrapper.sh

4. Reload ~/.bashrc and create virtual env named kafka_support
    - source ~/.bashrc
    - mkvirtualenv kafka_support

5. To install packages and run
    - workon kafka_support
    - pip install -r requirements.txt (from KafkaLocalStorage directory)
    - run: python run.py -h