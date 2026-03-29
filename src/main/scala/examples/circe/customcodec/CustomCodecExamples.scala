package examples.circe.customcodec

import examples.circe.support.ExampleSupport._
import io.circe.parser.decode
import io.circe.syntax._
import io.circe.{Decoder, Encoder, Json}

import java.time.Instant
import scala.util.Try

/**
 * References:
 * - https://github.com/circe/circe
 * - https://circe.github.io/circe/codecs/custom-codecs.html
 */
object CustomCodecExamples {
  final case class LegacyUser(firstName: String, lastName: String, createdAt: Instant)

  private object LegacyUser {
    implicit val instantEncoder: Encoder[Instant] =
      Encoder.encodeString.contramap[Instant](_.toString)

    implicit val instantDecoder: Decoder[Instant] =
      Decoder.decodeString.emapTry(value => Try(Instant.parse(value)))

    implicit val encoder: Encoder[LegacyUser] =
      Encoder.instance[LegacyUser] { user =>
        Json.obj(
          "first_name" -> Json.fromString(user.firstName),
          "last_name" -> Json.fromString(user.lastName),
          "created_at" -> user.createdAt.asJson
        )
      }

    implicit val decoder: Decoder[LegacyUser] =
      Decoder.instance[LegacyUser] { cursor =>
        for {
          firstName <- cursor.get[String]("first_name")
          lastName <- cursor.get[String]("last_name")
          createdAt <- cursor.get[Instant]("created_at")
        } yield LegacyUser(firstName, lastName, createdAt)
      }
  }

  final case class ServiceWindow(startHour: Int, endHour: Int)

  private object ServiceWindow {
    implicit val encoder: Encoder.AsObject[ServiceWindow] =
      Encoder.forProduct2("start_hour", "end_hour")(window => (window.startHour, window.endHour))
    implicit val decoder: Decoder[ServiceWindow] =
      Decoder.forProduct2("start_hour", "end_hour")(ServiceWindow.apply)
  }

  def main(args: Array[String]): Unit = {
    section("Manual Encoder.instance / Decoder.instance")
    val user = LegacyUser("Kaz", "M", Instant.parse("2026-03-27T10:15:30Z"))
    showJson("LegacyUser JSON", user.asJson)
    showResult("Decode LegacyUser", user.asJson.as[LegacyUser])

    section("forProductN for custom key names")
    val window = ServiceWindow(9, 18)
    showJson("ServiceWindow JSON", window.asJson)
    showResult("Decode ServiceWindow", decode[ServiceWindow]("""{"start_hour":9,"end_hour":18}"""))
  }
}
