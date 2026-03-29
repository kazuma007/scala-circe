package examples.circe.accumulatingdecoder

import examples.circe.support.ExampleSupport._
import cats.syntax.apply._
import io.circe.Decoder
import io.circe.parser.parse
/**
 * References:
 * - https://github.com/circe/circe
 * - https://circe.github.io/circe/codecs/custom-codecs.html
 */
object AccumulatingDecoderExamples {
  final case class Registration(email: String, age: Int, country: String)

  object Registration {
    implicit val decoder: Decoder[Registration] =
      Decoder.accumulatingInstance[Registration] { cursor =>
        (
          cursor.getAcc[String]("email"),
          cursor.getAcc[Int]("age"),
          cursor.getAcc[String]("country")
        ).mapN(Registration.apply)
      }
  }

  def main(args: Array[String]): Unit = {
    section("Accumulating decoder success")
    val valid = """{"email":"team@example.com","age":10,"country":"AT"}"""
    val validJson = parse(valid).fold(throw _, identity)
    println(s"decodeAccumulating: ${Decoder[Registration].decodeAccumulating(validJson.hcursor)}")

    section("Accumulating decoder failure")
    val invalid = """{"email":12,"country":false}"""
    val invalidJson = parse(invalid).fold(throw _, identity)
    println(s"decodeAccumulating: ${Decoder[Registration].decodeAccumulating(invalidJson.hcursor)}")
  }
}
