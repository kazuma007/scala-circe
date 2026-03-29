package examples.circe.support

import io.circe.{Decoder, Encoder, Json, Printer}

object ExampleSupport {
  private val prettyPrinter = Printer.spaces2SortKeys

  def section(name: String): Unit = {
    println("")
    println(s"== $name ==")
  }

  def showJson(label: String, json: Json): Unit =
    println(s"$label: ${prettyPrinter.print(json)}")

  def showEncoded[A: Encoder](label: String, value: A): Unit =
    showJson(label, Encoder[A].apply(value))

  def showResult[A](label: String, result: Either[io.circe.Error, A]): Unit =
    println(s"$label: $result")

  def showDecode[A: Decoder](label: String, input: String): Unit =
    println(s"$label: ${io.circe.parser.decode[A](input)}")

  def showOption(label: String, result: Option[Json]): Unit =
    println(s"$label: ${result.map(prettyPrinter.print)}")
}
