package examples.circe.cursor

import examples.circe.support.ExampleSupport._
import io.circe.Json

/**
 * References:
 * - https://github.com/circe/circe
 * - https://circe.github.io/circe/api/io/circe/HCursor.html
 */
object CursorExamples {

  def main(args: Array[String]): Unit = {
    val document = Json.obj(
      "service" -> Json.obj(
        "name" -> Json.fromString("inventory"),
        "owners" -> Json.arr(
          Json.obj("name" -> Json.fromString("Ava")),
          Json.obj("name" -> Json.fromString("Noah"))
        ),
        "version" -> Json.fromInt(1)
      )
    )

    section("Navigate with cursors")
    val cursor = document.hcursor
    println(s"Service name: ${cursor.downField("service").get[String]("name")}")
    println(s"First owner: ${cursor.downField("service").downField("owners").downN(0).get[String]("name")}")

    section("Update JSON with withFocus")
    val updated = cursor
      .downField("service")
      .downField("version")
      .withFocus { json =>
        json.asNumber.flatMap(_.toInt).map(value => Json.fromInt(value + 1)).getOrElse(json)
      }
      .top
    showOption("Updated document", updated)
  }
}
