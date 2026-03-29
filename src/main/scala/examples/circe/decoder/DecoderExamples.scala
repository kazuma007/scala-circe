package examples.circe.decoder

import examples.circe.support.ExampleSupport._
import io.circe.Decoder
import io.circe.parser.decode

/**
 * References:
 * - https://github.com/circe/circe
 * - https://circe.github.io/circe/codecs/custom-codecs.html
 */
object DecoderExamples {
  final case class EmailAddress(value: String) extends AnyVal
  final case class Product(id: Long, name: String, quantity: Int)

  object EmailAddress {
    implicit val decoder: Decoder[EmailAddress] =
      Decoder.decodeString.emap { raw =>
        Either.cond(raw.contains("@"), EmailAddress(raw), s"Invalid email address: $raw")
      }
  }

  object Product {
    implicit val decoder: Decoder[Product] =
      Decoder.forProduct3("id", "name", "quantity")(Product.apply)
  }

  def main(args: Array[String]): Unit = {
    section("Validated string decoder")
    showResult("EmailAddress", decode[EmailAddress]("\"team@circe.dev\""))
    showResult("Invalid EmailAddress", decode[EmailAddress]("\"team-at-circe.dev\""))

    section("forProductN decoder")
    showResult("Product", decode[Product]("""{"id": 7, "name": "notebook", "quantity": 3}"""))
  }
}
