package joehonour.newcastleuniversity.anomalydetectionservice.anomaly_predictors.outputs

import org.apache.spark.mllib.linalg

case class IdentifiableVector(uuid: String, vector: linalg.Vector)