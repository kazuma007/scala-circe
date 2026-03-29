package examples.circe.adt

import cats.syntax.functor._
import examples.circe.support.ExampleSupport._
import io.circe.generic.semiauto._
import io.circe.parser.decode
import io.circe.syntax._
import io.circe.{Decoder, Encoder}

/**
 * References:
 * - https://github.com/circe/circe
 * - https://circe.github.io/circe/codecs/adt.html
 */
object AdtExamples {
  sealed trait Event

  final case class Foo(i: Int) extends Event

  final case class Bar(s: String) extends Event

  private object Foo {
    implicit val encoder: Encoder.AsObject[Foo] = deriveEncoder[Foo]
    implicit val decoder: Decoder[Foo] = deriveDecoder[Foo]
  }

  object Bar {
    implicit val encoder: Encoder.AsObject[Bar] = deriveEncoder[Bar]
    implicit val decoder: Decoder[Bar] = deriveDecoder[Bar]
  }

  object Event {
    implicit val encoder: Encoder[Event] = Encoder.instance {
      case foo: Foo => foo.asJson
      case bar: Bar => bar.asJson
    }

    implicit val decoder: Decoder[Event] =
      List[Decoder[Event]](
        Decoder[Foo].widen,
        Decoder[Bar].widen
      ).reduceLeft(_ or _)
  }

  def main(args: Array[String]): Unit = {
    section("Encode ADT")
    val event: Event = Foo(1000)
    showJson("Event JSON", event.asJson)

    section("Decode ADT")
    showResult("Decode Foo", decode[Event]("""{"i":1000}"""))
    showResult("Decode Bar", decode[Event]("""{"s":"ok"}"""))
  }
}
