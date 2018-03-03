"""
Responsible for writing the messages to an output file
"""

import json
import datetime
from typing import Dict


def produce_json_file_output(file_identifier: str, json_contents: Dict):
    """
    Given a file path and json contents, writes the json contents to the file
    :param file_identifier: the file to write to
    :param json_contents: the json contents
    """
    file_name = "{0}__{1}.json".format(str(datetime.date.today()), file_identifier)

    with open(file_name, 'w') as file:
        json.dump(json_contents, file, indent=4)
