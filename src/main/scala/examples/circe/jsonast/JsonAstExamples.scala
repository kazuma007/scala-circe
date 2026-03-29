package examples.circe.jsonast

import examples.circe.support.ExampleSupport._
import io.circe.{Json, JsonObject}

/**
 * References:
 * - https://github.com/circe/circe
 * - https://circe.github.io/circe/api/io/circe/Json.html
 */
object JsonAstExamples {
  def main(args: Array[String]): Unit = {
    section("Build JSON manually")
    val serviceJson = Json.obj(
      "name" -> Json.fromString("catalog"),
      "ports" -> Json.arr(Json.fromInt(8080), Json.fromInt(8081)),
      "healthy" -> Json.fromBoolean(true)
    )
    showJson("serviceJson", serviceJson)

    section("Merge JSON documents")
    val defaults = Json.obj("timeoutMs" -> Json.fromInt(1000), "retries" -> Json.fromInt(3))
    val overrides = Json.obj("retries" -> Json.fromInt(5))
    showJson("deepMerge", defaults.deepMerge(overrides))

    section("Transform object fields")
    val uppercasedKeys = serviceJson.mapObject(_.toMap.foldLeft(JsonObject.empty) {
      case (acc, (key, value)) => acc.add(key.toUpperCase, value)
    })
    showJson("uppercasedKeys", uppercasedKeys)
  }
}
