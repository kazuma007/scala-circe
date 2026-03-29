package examples.circe.printer

import examples.circe.support.ExampleSupport._
import io.circe.{Json, Printer}

/**
 * References:
 * - https://github.com/circe/circe
 * - https://circe.github.io/circe/api/io/circe/Printer.html
 */
object PrinterExamples {
  def main(args: Array[String]): Unit = {
    val json = Json.obj(
      "name" -> Json.fromString("circe"),
      "features" -> Json.arr(Json.fromString("parser"), Json.fromString("codecs")),
      "optional" -> Json.Null
    )

    section("Built-in printers")
    println(s"noSpaces: ${Printer.noSpaces.print(json)}")
    println(s"spaces2SortKeys: ${Printer.spaces2SortKeys.print(json)}")

    section("Custom printer")
    val customPrinter = Printer.spaces2.copy(dropNullValues = true)
    println(s"custom: ${customPrinter.print(json)}")
    showJson("original", json)
  }
}
