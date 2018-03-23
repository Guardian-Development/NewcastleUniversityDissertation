package newcastleuniversity.joehonour.alerts.outputs

import java.util.Properties

import javax.mail.internet.InternetAddress
import newcastleuniversity.joehonour.messages.AnomalyScore
import courier._, Defaults._

class EmailNotificationOutput(private val properties: Properties) extends Serializable {

  def sendEmailForAnomaly(anomalyScore: AnomalyScore) : Unit = {

    val mailer = Mailer(properties.getProperty("smtp.server.address"), properties.getProperty("smtp.server.port").toInt)
      .auth(true)
      .as(properties.getProperty("smtp.from.address"), properties.getProperty("smtp.from.password"))
      .startTtls(true)()

    mailer(Envelope.from(new InternetAddress(properties.getProperty("smtp.from.address")))
      .to(new InternetAddress(properties.getProperty("smtp.to.address")))
      .subject("Alert Detected within Video Stream")
      .content(Text(alertMessage(anomalyScore, properties.getProperty("anomaly.database.url")))))
      .onFailure {
        case t => println(t)
      }
  }

  private def alertMessage(anomalyScore: AnomalyScore, databaseUrl: String) : String = {
    s"""
       |An anomaly has been detected:
       |
       id: ${anomalyScore.uuid}
       cluster: ${anomalyScore.cluster}
       score: ${anomalyScore.score}

       To investigate the data please go to: $databaseUrl

       Alert sent autonomously by
       Joe Honour Dissertation Project

     """.stripMargin
  }
}
