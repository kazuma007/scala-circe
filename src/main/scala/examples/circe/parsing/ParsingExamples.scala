package examples.circe.parsing

import examples.circe.support.ExampleSupport._
import io.circe.Decoder
import io.circe.generic.semiauto._
import io.circe.parser.{decode, parse}

/**
 * References:
 * - https://github.com/circe/circe
 * - https://circe.github.io/circe/codecs/index.html
 * - https://circe.github.io/circe/
 */
object ParsingExamples {
  final case class BuildInfo(name: String, version: String)

  object BuildInfo {
    implicit val decoder: Decoder[BuildInfo] = deriveDecoder[BuildInfo]
  }

  def main(args: Array[String]): Unit = {
    section("parse")
    showResult("Valid JSON", parse("""{"language":"Scala","major":2}"""))
    showResult("Invalid JSON", parse("""{"language":"Scala",}"""))

    section("decode from String")
    showResult("BuildInfo", decode[BuildInfo]("""{"name":"scala-circe","version":"0.1.0"}"""))
    showResult("List[Int]", decode[List[Int]]("[1,2,3,4]"))
  }
}
