package newcastleuniversity.joehonour.movement_detection.movements

trait DetectedMovement {
  def uuid: String
  def movement_type: String
  def from_location_x: Double
  def from_location_y: Double
  def to_location_x: Double
  def to_location_y: Double
  def average_displacement: Double
}


