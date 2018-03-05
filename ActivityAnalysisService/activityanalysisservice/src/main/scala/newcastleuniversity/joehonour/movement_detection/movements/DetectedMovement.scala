package newcastleuniversity.joehonour.movement_detection.movements

trait DetectedMovement {
  def uuid: String
  def movement_type: String
  def fromLocationX: Double
  def fromLocationY: Double
  def toLocationX: Double
  def toLocationY: Double
  def averageDisplacement: Double
}


