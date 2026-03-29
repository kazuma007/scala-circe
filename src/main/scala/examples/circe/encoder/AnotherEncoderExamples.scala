package examples.circe.encoder

import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder
import io.circe.syntax._

object AnotherEncoderExamples {

  final case class Parent(animals: Seq[Animal])

  sealed trait Animal

  final case class Dog(name: String, barkVolume: Int) extends Animal

  final case class Cat(name: String, age: Int) extends Animal

  object Dog {
    implicit val encoder: Encoder[Dog] = deriveEncoder
  }

  object Cat {
    implicit val encoder: Encoder[Cat] = deriveEncoder
  }

  // ---------------------------------------------------------------------------
  // Option 1: Default ADT derivation
  // ---------------------------------------------------------------------------
  //
  // implicit val encoder: Encoder[Animal] = deriveEncoder[Animal]
  //
  // Result:
  //   {"Cat":{"name":"cat","age":1}}
  //
  // Behavior:
  //   - Fully automatic.
  //   - Adds a subtype wrapper ("Cat", "Dog"), which we do not want here.
  //   - Less convenient when the API expects the subtype fields directly.
  //
  // Use this when:
  //   - You want the JSON to explicitly include the subtype name.

  // ---------------------------------------------------------------------------
  // Option 2: Generic shapeless / circe-shapes dispatch
  // ---------------------------------------------------------------------------
  //
  // object Animal {
  //   implicit val encoder: Encoder[Animal] =
  //     Encoder.instance { animal =>
  //       Generic[Animal].to(animal).asJson
  //     }
  // }
  //
  // Result:
  //   {"name":"cat","age":1}
  //
  // Behavior:
  //   - Avoids the wrapper.
  //   - More generic for larger ADTs.
  //   - Harder to understand at a glance.
  //   - Pulls in shapeless-based generic machinery.
  //   - Less obvious to maintainers how subtype selection happens.
  //
  // Use this when:
  //   - You have a large ADT and want a generic no-wrapper solution.

  // ---------------------------------------------------------------------------
  // Option 3: Explicit subtype delegation
  // ---------------------------------------------------------------------------
  //
  // `Animal` is a sealed trait, so it does not have one fixed set of fields.
  // At runtime, each value is actually a `Dog` or `Cat`, and each subtype
  // already has its own derived encoder.
  //
  // This encoder simply routes the value to the correct subtype encoder.
  // We are not manually building JSON; we are only choosing which already-
  // derived encoder to use.
  //
  // Why we need this:
  //   - `Parent` contains `Seq[Animal]`.
  //   - Circe therefore needs an `Encoder[Animal]` for each element.
  //   - If we use default ADT derivation, the JSON includes a wrapper key
  //     such as `"Cat"` or `"Dog"`.
  //   - By delegating to the subtype encoders, we keep the JSON flat:
  //       {"name":"cat","age":1}
  //     instead of:
  //       {"Cat":{"name":"cat","age":1}}
  object Animal {
    implicit val encoder: Encoder[Animal] = Encoder.instance {
      case dog: Dog => dog.asJson
      case cat: Cat => cat.asJson
    }
  }

  object Parent {
    implicit val encoder: Encoder[Parent] = deriveEncoder
  }

  def main(args: Array[String]): Unit = {
    val parent = Parent(
      animals = Seq(
        Cat("cat", 1),
        Dog("dog", 2)
      )
    )

    println(parent.asJson.spaces2)
  }
}