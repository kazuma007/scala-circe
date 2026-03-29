package examples.circe.literal

import examples.circe.support.ExampleSupport._
import io.circe.Decoder
import io.circe.literal._
import io.circe.parser.decode

/**
 * References:
 * - https://github.com/circe/circe
 * - https://circe.github.io/circe/api/io/circe/literal/index.html
 */
object LiteralExamples {
  final case class Environment(name: String, region: String)

  object Environment {
    implicit val decoder: Decoder[Environment] =
      Decoder.forProduct2("name", "region")(Environment.apply)
  }

  def main(args: Array[String]): Unit = {
    section("json string interpolation")
    val document = json"""
      {
        "name": "production",
        "region": "eu-central-1",
        "enabled": true
      }
    """
    showJson("literal JSON", document)

    section("Decode interpolated JSON")
    showResult("Environment", decode[Environment](document.noSpaces))
  }
}
