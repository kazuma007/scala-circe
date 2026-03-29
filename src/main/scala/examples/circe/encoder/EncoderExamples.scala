package examples.circe.encoder

import examples.circe.support.ExampleSupport._
import io.circe.syntax._
import io.circe.{Encoder, Json, JsonObject}

/**
 * References:
 * - https://github.com/circe/circe
 * - https://circe.github.io/circe/codecs/custom-codecs.html
 */
object EncoderExamples {
  final case class Sku(value: String) extends AnyVal

  final case class InventoryItem(sku: Sku, quantity: Int, active: Boolean)

  final case class UserProfile(id: Long, name: String, email: String)

  final case class TemperatureCelsius(value: Double) extends AnyVal

  final case class AuditLog(event: String, level: String, internalId: Long)

  object Sku {
    implicit val encoder: Encoder[Sku] =
      Encoder.encodeString.contramap[Sku](_.value)
  }

  object InventoryItem {
    implicit val encoder: Encoder.AsObject[InventoryItem] =
      Encoder.forProduct3("sku", "quantity", "active")(item =>
        (item.sku.value, item.quantity, item.active)
      )
  }

  object UserProfile {
    implicit val encoder: Encoder.AsObject[UserProfile] =
      Encoder.AsObject.instance[UserProfile] { profile =>
        JsonObject(
          "id" -> Json.fromLong(profile.id),
          "name" -> Json.fromString(profile.name),
          "email" -> Json.fromString(profile.email)
        )
      }
  }

  object TemperatureCelsius {
    implicit val encoder: Encoder[TemperatureCelsius] =
      Encoder.encodeJson.contramap[TemperatureCelsius] { value =>
        Json.obj(
          "celsius" -> Json.fromDoubleOrNull(value.value),
          "fahrenheit" -> Json.fromDoubleOrNull(value.value * 9d / 5d + 32d)
        )
      }
  }

  final case class Coordinates(lat: Double, lon: Double)

  object Coordinates {
    implicit val encoder: Encoder[Coordinates] =
      Encoder.instance[Coordinates] { coordinates =>
        Json.obj(
          "lat" -> Json.fromDoubleOrNull(coordinates.lat),
          "lon" -> Json.fromDoubleOrNull(coordinates.lon)
        )
      }
  }

  object AuditLog {
    implicit val encoder: Encoder[AuditLog] =
      Encoder.forProduct3("event", "level", "internal_id")((value: AuditLog) =>
        (value.event, value.level, value.internalId)
      ).mapJson(_.deepDropNullValues)
  }

  def main(args: Array[String]): Unit = {
    section("Contramap encoder")
    showEncoded("Sku", Sku("book-001"))

    section("forProductN encoder")
    showJson("InventoryItem JSON", InventoryItem(Sku("book-001"), 42, active = true).asJson)

    section("Encoder.AsObject.instance")
    showJson("UserProfile JSON", UserProfile(10L, "Kaz", "kaz@example.com").asJson)

    section("Encoder.encodeJson.contramap")
    showEncoded("TemperatureCelsius", TemperatureCelsius(21.5))

    section("Encoder.instance")
    showEncoded("Coordinates", Coordinates(48.2082, 16.3738))

    section("mapJson post-processing")
    showEncoded("AuditLog", AuditLog("login", "info", 999L))
  }
}
