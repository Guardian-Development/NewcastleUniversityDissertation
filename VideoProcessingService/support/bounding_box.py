"""
Provides a bounding box which can be used to represent a detected object
"""
from collections import namedtuple


class BoundingBox(namedtuple('BoundingBox',
                             ["x_position", "y_position", "width", "height", "item_type", "uuid"])):
    """
    A BoundingBox which stores the position, size, type and identifier of an object
    """
    def __new__(cls, x_position, y_position, width, height, item_type, uuid=None):
        return super(BoundingBox, cls).__new__(cls, x_position, y_position, width, height, item_type, uuid)


def bounding_boxes_collide(box1: BoundingBox, box2: BoundingBox) -> bool:
    """
    Detects whether 2 bounding boxes collide based on their location

    :param box1: first box
    :param box2: second box
    :return: true if they do collide, else false
    """
    box1_left_x, box1_bottom_y, box1_right_x, box1_top_y = \
        (box1.x_position, box1.y_position, box1.x_position + box1.width, box1.y_position + box1.height)
    box2_left_x, box2_bottom_y, box2_right_x, box2_top_y = \
        (box2.x_position, box2.y_position, box2.x_position + box2.width, box2.y_position + box2.height)

    return (box1_left_x <= box2_right_x and
            box1_right_x >= box2_left_x and
            box1_top_y >= box2_bottom_y and
            box1_bottom_y <= box2_top_y)


def intersection_over_union(box1: BoundingBox, box2: BoundingBox) -> float:
    """
    Performs intersection over union calculation to give a normalised value between 0 and 1
    of how much 2 boxes overlap

    :param box1: first box
    :param box2: second box
    :return: float between 0 and 1, closer to 0 the less the boxes overlap
    """
    box1_left_x, box1_bottom_y, box1_right_x, box1_top_y = \
        (box1.x_position, box1.y_position, box1.x_position + box1.width, box1.y_position + box1.height)
    box2_left_x, box2_bottom_y, box2_right_x, box2_top_y = \
        (box2.x_position, box2.y_position, box2.x_position + box2.width, box2.y_position + box2.height)

    intersection_left_x = max(box1_left_x, box2_left_x)
    intersection_bottom_y = max(box1_bottom_y, box2_bottom_y)
    intersection_right_x = min(box1_right_x, box2_right_x)
    intersection_top_y = min(box1_top_y, box2_top_y)

    intersection_area = (intersection_right_x - intersection_left_x + 1) * \
                        (intersection_top_y - intersection_bottom_y + 1)

    box1_area = (box1_right_x - box1_left_x + 1) * \
                (box1_top_y - box1_bottom_y + 1)
    box2_area = (box2_right_x - box2_left_x + 1) * \
                (box2_top_y - box2_bottom_y + 1)

    return intersection_area / float(box1_area + box2_area - intersection_area)


def convert_to_dict(box: BoundingBox) -> dict:
    """
    Converts a bounding box to a dictionary

    :param box: the box to convert
    :return: a dictionary representation of the bounding box
    """
    return {
        "uuid": str(box.uuid),
        "x_position": int(box.x_position),
        "y_position": int(box.y_position),
        "width": int(box.width),
        "height": int(box.height),
        "type": str(box.item_type)
    }
