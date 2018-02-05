from typing import Tuple
from collections import namedtuple

BoundingBox = namedtuple(
    "BoundingBox",
    "x_position y_position width height item_type")


def bounding_boxes_collide(box1: Tuple[float, float, float, float],
                           box2: Tuple[float, float, float, float]) -> bool:
    box1_left_x, box1_bottom_y, box1_right_x, box1_top_y = box1
    box2_left_x, box2_bottom_y, box2_right_x, box2_top_y = box2

    return (box1_left_x <= box2_right_x and
            box1_right_x >= box2_left_x and
            box1_top_y >= box2_bottom_y and
            box1_bottom_y <= box2_top_y)


def intersection_over_union(box1: Tuple[float, float, float, float],
                            box2: Tuple[float, float, float, float]) -> float:
    box1_left_x, box1_bottom_y, box1_right_x, box1_top_y = box1
    box2_left_x, box2_bottom_y, box2_right_x, box2_top_y = box2

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
