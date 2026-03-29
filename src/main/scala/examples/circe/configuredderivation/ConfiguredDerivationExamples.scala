package examples.circe.configuredderivation

import examples.circe.support.ExampleSupport._
import io.circe.Decoder
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto._
import io.circe.parser.decode
import io.circe.syntax._

/**
 * References:
 * - https://github.com/circe/circe
 * - https://circe.github.io/circe/codecs/custom-codecs.html
 */
object ConfiguredDerivationExamples {
  final case class ServiceConfig(serviceName: String, maxRetries: Int = 3)

  private object SnakeCaseDefaults {
    implicit val config: Configuration = Configuration.default.withSnakeCaseMemberNames.withDefaults
    implicit val encoder: io.circe.Encoder.AsObject[ServiceConfig] = deriveConfiguredEncoder[ServiceConfig]
    implicit val decoder: Decoder[ServiceConfig] = deriveConfiguredDecoder[ServiceConfig]
  }

  sealed trait Notification

  final case class Email(address: String) extends Notification

  final case class Sms(number: String) extends Notification

  private object DiscriminatorEncoding {
    implicit val config: Configuration = Configuration.default.withDiscriminator("kind")
    implicit val emailEncoder: io.circe.Encoder.AsObject[Email] = deriveConfiguredEncoder[Email]
    implicit val emailDecoder: Decoder[Email] = deriveConfiguredDecoder[Email]
    implicit val smsEncoder: io.circe.Encoder.AsObject[Sms] = deriveConfiguredEncoder[Sms]
    implicit val smsDecoder: Decoder[Sms] = deriveConfiguredDecoder[Sms]
    implicit val notificationEncoder: io.circe.Encoder.AsObject[Notification] = deriveConfiguredEncoder[Notification]
    implicit val notificationDecoder: Decoder[Notification] = deriveConfiguredDecoder[Notification]
  }

  final case class StrictPayload(id: Long, name: String)

  private object StrictDecoding {
    implicit val config: Configuration = Configuration.default.copy(strictDecoding = true)
    implicit val decoder: Decoder[StrictPayload] = deriveConfiguredDecoder[StrictPayload]
  }

  def main(args: Array[String]): Unit = {
    implicit val snakeCaseDefaultsEncoder: io.circe.Encoder.AsObject[ServiceConfig] = SnakeCaseDefaults.encoder
    implicit val snakeCaseDefaultsDecoder: Decoder[ServiceConfig] = SnakeCaseDefaults.decoder
    implicit val discriminatorNotificationEncoder: io.circe.Encoder.AsObject[Notification] =
      DiscriminatorEncoding.notificationEncoder
    implicit val discriminatorNotificationDecoder: Decoder[Notification] =
      DiscriminatorEncoding.notificationDecoder
    implicit val strictPayloadDecoder: Decoder[StrictPayload] = StrictDecoding.decoder

    section("Snake case and defaults")
    val config = ServiceConfig("search")
    showJson("ServiceConfig JSON", config.asJson)
    showResult("Decode defaults", decode[ServiceConfig]("""{"service_name":"search"}"""))

    section("Discriminator for ADTs")
    val notification: Notification = Email("team@example.com")
    showJson("Notification JSON", notification.asJson)
    showResult("Decode Notification", decode[Notification]("""{"kind":"Email","address":"team@example.com"}"""))

    section("Strict decoding")
    showResult("StrictPayload", decode[StrictPayload]("""{"id":1,"name":"ok"}"""))
    showResult("StrictPayload with extra field", decode[StrictPayload]("""{"id":1,"name":"ok","extra":true}"""))
  }
}
