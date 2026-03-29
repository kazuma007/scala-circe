package examples.circe.encoder

import examples.circe.support.ExampleSupport._
import io.circe.{Encoder, Json}
import io.circe.generic.semiauto.deriveEncoder
import io.circe.syntax._

/**
 * References:
 * - https://github.com/circe/circe
 * - https://circe.github.io/circe/codecs/custom-codecs.html
 */
object EncoderExamples {
  final case class Item(value: Option[String], number: Int)

  final case class InventoryItems(items: Seq[Item])

  object Item {
    implicit val encoder: Encoder[Item] = Encoder.instance { item =>
      Json.obj(
        List(
          Some("number" -> Json.fromInt(item.number)),
          item.value.map("value" -> Json.fromString(_))
        ).flatten: _*
      )
    }
  }

  object InventoryItems {
    implicit val encoder: Encoder[InventoryItems] = deriveEncoder
  }

  def main(args: Array[String]): Unit = {
    section("InventoryItems encoder")
    val items = Seq(
      Item(Some("a"), 1),
      Item(None, 2),
      Item(Some("b"), 3)
    )

    showJson("JSON", InventoryItems(items).asJson)
  }
}
