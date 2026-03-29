package examples.circe.automaticderivation

import examples.circe.support.ExampleSupport._
import io.circe.generic.auto._
import io.circe.parser.decode
import io.circe.syntax._

/**
 * References:
 * - https://github.com/circe/circe
 * - https://circe.github.io/circe/codecs/auto-derivation.html
 */
object AutomaticDerivationExamples {
  final case class Person(name: String)

  private final case class Greeting(salutation: String, person: Person, exclamationMarks: Int)

  sealed trait PaymentMethod

  private final case class Card(last4: String, brand: String) extends PaymentMethod

  final case class Wire(iban: String) extends PaymentMethod

  def main(args: Array[String]): Unit = {
    section("Nested products")
    val greeting = Greeting("Hello", Person("Circe"), 3)
    showJson("Greeting JSON", greeting.asJson)
    showResult("Decode Greeting", greeting.asJson.as[Greeting])

    section("ADT with automatic derivation")
    val payment: PaymentMethod = Card("4242", "visa")
    showJson("Payment JSON", payment.asJson)
    showResult("Decode PaymentMethod", decode[PaymentMethod]("""{"last4":"4242","brand":"visa"}"""))
  }
}
