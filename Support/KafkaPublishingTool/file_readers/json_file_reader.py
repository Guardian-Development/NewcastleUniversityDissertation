"""
Responsible for reading files into the program
"""

import json
from typing import Dict


def read_json_file(file_path: str) -> Dict:
    """
    Reads a JSON file and returns a Dict representing the file
    :param file_path: the JSON file to read
    :return: a dict representing the JSON file
    """
    with open(file_path, 'r') as file:
        data = file.read()
        return json.loads(data)
