package newcastleuniversity.joehonour.messages

case class MovementObserved(movement_uuid: String,
                            object_uuid: String,
                            movement_type: String,
                            from_position_x: Double,
                            from_position_y: Double,
                            to_position_x: Double,
                            to_position_y: Double,
                            average_displacement: Double)
