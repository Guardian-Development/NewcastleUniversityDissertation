package joehonour.newcastleuniversity.anomalydetectionservice.anomaly_predictors

import joehonour.newcastleuniversity.anomalydetectionservice.anomaly_predictors.outputs.{IdentifiableDistance, IdentifiableVector}
import org.apache.spark.mllib.feature.Normalizer
import org.apache.spark.mllib.clustering.StreamingKMeans
import org.apache.spark.mllib.linalg
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.streaming.dstream.DStream


object KMeansStreamPredictorBuilder {

  def buildKMeansDistancePredictor[T](inputStream: DStream[T],
                                      kMeansProducer: () => StreamingKMeans,
                                      convertToVector: (T) => linalg.Vector,
                                      convertToIdentifiableVector: (T) => IdentifiableVector): DStream[IdentifiableDistance] = {

    val normalizer = new Normalizer()
    val kMeansModel = kMeansProducer()

    inputStream
      .map { convertToIdentifiableVector }
      .map { m => IdentifiableVector(m.uuid, normalizer.transform(m.vector)) }
      .map { m => (m.uuid, m.vector, kMeansModel.latestModel().predict(m.vector)) }
      .map { p => IdentifiableDistance(p._1, p._3, Vectors.sqdist(p._2, kMeansModel.latestModel().clusterCenters(p._3))) }
  }
}
