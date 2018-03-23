package newcastleuniversity.joehonour.alerts.detectors

import java.util.Properties

import newcastleuniversity.joehonour.messages.AnomalyScore

class AlertDetector(private val properties: Properties) extends Serializable {

  def anomalyRequiresAlert(anomalyScore: AnomalyScore) : Boolean = {
    val anomalyProperty = s"anomaly.cluster.${anomalyScore.cluster}.anomalous"
    val anomalyBoundary = properties.getProperty("anomaly.cluster.alert.boundary").toDouble
    val clusterRequiresAlert = properties.getProperty(anomalyProperty).toBoolean

    clusterRequiresAlert || anomalyScore.score > anomalyBoundary
  }
}
