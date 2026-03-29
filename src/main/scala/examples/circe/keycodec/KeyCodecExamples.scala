package examples.circe.keycodec

import examples.circe.support.ExampleSupport._
import io.circe.parser.decode
import io.circe.syntax._
import io.circe.{KeyDecoder, KeyEncoder}

/**
 * References:
 * - https://github.com/circe/circe
 * - https://circe.github.io/circe/codecs/custom-codecs.html
 * - https://circe.github.io/circe/api/io/circe/KeyEncoder.html
 * - https://circe.github.io/circe/api/io/circe/KeyDecoder.html
 */
object KeyCodecExamples {
  sealed trait MetricKey {
    def value: String
  }

  final case class ThroughputRps(value: String) extends MetricKey
  final case class LatencyMs(value: String) extends MetricKey

  object MetricKey {
    implicit val keyEncoder: KeyEncoder[MetricKey] =
      KeyEncoder.instance[MetricKey](_.value)

    implicit val keyDecoder: KeyDecoder[MetricKey] =
      KeyDecoder.instance[MetricKey] { raw =>
        if (raw.startsWith("throughput")) Some(ThroughputRps(raw))
        else if (raw.startsWith("latency")) Some(LatencyMs(raw))
        else None
      }
  }

  def main(args: Array[String]): Unit = {
    implicit val keyEncoder: KeyEncoder[MetricKey] = MetricKey.keyEncoder
    implicit val keyDecoder: KeyDecoder[MetricKey] = MetricKey.keyDecoder

    section("Encode Map with custom keys")
    val metrics = Map[MetricKey, Double](
      ThroughputRps("throughput.p95") -> 1234.5,
      LatencyMs("latency.p99") -> 42.1
    )
    showJson("metrics JSON", metrics.asJson)

    section("Decode Map with custom keys")
    showResult(
      "metrics",
      decode[Map[MetricKey, Double]]("""{"throughput.p95":1234.5,"latency.p99":42.1}""")
    )
  }
}
