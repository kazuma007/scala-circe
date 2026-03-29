package examples.circe.semiautoderivation

import examples.circe.support.ExampleSupport._
import io.circe.generic.JsonCodec
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.{deriveConfiguredDecoder, deriveConfiguredEncoder, deriveUnwrappedDecoder, deriveUnwrappedEncoder}
import io.circe.generic.semiauto._
import io.circe.parser.decode
import io.circe.syntax._
import io.circe.{Codec, Decoder, Encoder}


/**
 * References:
 * - https://github.com/circe/circe
 * - https://circe.github.io/circe/codecs/semiauto-derivation.html
 */
object SemiAutoDerivationExamples {
  final case class AccountId(value: Long) extends AnyVal

  object AccountId {
    implicit val encoder: Encoder[AccountId] = deriveUnwrappedEncoder[AccountId]
    implicit val decoder: Decoder[AccountId] = deriveUnwrappedDecoder[AccountId]
  }

  final case class Address(city: String, country: String)

  object Address {
    implicit val codec: Codec.AsObject[Address] = deriveCodec[Address]
  }

  final case class Account(id: AccountId, owner: String, address: Address)

  object Account {
    implicit val encoder: Encoder.AsObject[Account] = deriveEncoder[Account]
    implicit val decoder: Decoder[Account] = deriveDecoder[Account]
  }

  @JsonCodec final case class AuditEvent(action: String, actor: String)

  final case class RenamedFields(serviceName: String, maxRetries: Int)

  object RenamedFields {
    implicit val config: Configuration = Configuration.default.withSnakeCaseMemberNames
    implicit val encoder: Encoder.AsObject[RenamedFields] = deriveConfiguredEncoder[RenamedFields]
    implicit val decoder: Decoder[RenamedFields] = deriveConfiguredDecoder[RenamedFields]
  }

  final case class ProductAlias(firstName: String, lastName: String)

  object ProductAlias {
    implicit val encoder: Encoder.AsObject[ProductAlias] =
      Encoder.forProduct2("first_name", "last_name")(value => (value.firstName, value.lastName))
    implicit val decoder: Decoder[ProductAlias] =
      Decoder.forProduct2("first_name", "last_name")(ProductAlias.apply)
  }

  def main(args: Array[String]): Unit = {
    section("deriveEncoder / deriveDecoder / deriveCodec")
    val account = Account(AccountId(1001L), "Kaz", Address("Vienna", "Austria"))
    showJson("Account JSON", account.asJson)
    showResult("Decode Account", account.asJson.as[Account])

    section("Value class with unwrapped codecs")
    showJson("AccountId JSON", AccountId(42L).asJson)
    showResult("Decode AccountId", decode[AccountId]("42"))

    section("@JsonCodec")
    val event = AuditEvent("login", "admin")
    showJson("AuditEvent JSON", event.asJson)
    showResult("Decode AuditEvent", event.asJson.as[AuditEvent])

    section("Configured semiauto derivation")
    showResult(
      "RenamedFields",
      decode[RenamedFields]("""{"service_name":"search","max_retries":5}""")
    )

    section("forProductN helper methods")
    val alias = ProductAlias("Scala", "Circe")
    showJson("ProductAlias JSON", alias.asJson)
    showResult("Decode ProductAlias", alias.asJson.as[ProductAlias])
  }
}
