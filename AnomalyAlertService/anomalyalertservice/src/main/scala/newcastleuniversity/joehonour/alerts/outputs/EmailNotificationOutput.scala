package newcastleuniversity.joehonour.alerts.outputs

import java.util.Properties

import newcastleuniversity.joehonour.messages.AnomalyScore

class EmailNotificationOutput(private val properties: Properties) extends Serializable {

  def sendEmailForAnomaly(anomalyScore: AnomalyScore) : Unit = {
    //TODO
  }
}
