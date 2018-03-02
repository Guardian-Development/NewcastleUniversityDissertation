import json
import datetime
from typing import Dict


def produce_json_file_output(file_identifier: str, json_contents: Dict):
    file_name = "{0}__{1}.json".format(str(datetime.date.today()), file_identifier)

    with open(file_name, 'w') as file:
        json.dump(json_contents, file, indent=4)
