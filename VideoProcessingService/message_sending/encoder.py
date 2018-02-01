from typing import Callable, List, Tuple

Encoder = Callable[[List[Tuple[float, float, float, float,str]]], str]

def encode_detected_objects(detected_objects: List[Tuple[float, float, float, float, str]]) -> str:
    pass