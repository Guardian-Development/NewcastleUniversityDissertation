# Kafka Local Storage Tool

A simple tool that takes a json file of ordered messages, and published them to Kafka to mock the publishing of a real service. You can use the KafkaLocalStorageTool to save a json file that this tool can make use of.

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

## Source File Format

The JSON source file containing the messages you wish to publish should be in the format:

```json
{
    "ordered_messages": [
        {
            "example_message" : "message"
        },
        {
            "example_message" : "message"
        }
    ]
}
```

As you can see the file should be an object with a single ordered_messages node. This is an array of JSON message objects you wish to send.

By default this is what the KafkaLocalStorageTool produces when creating a JSON file of a Kafka Topic.