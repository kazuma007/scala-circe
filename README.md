# scala-circe

`scala-circe` is a small Scala 2 example repository for learning and experimenting with [Circe](https://circe.github.io/circe/), a JSON library for Scala.

The project collects focused examples for common Circe tasks such as:

- encoding and decoding case classes
- custom encoders and decoders
- parsing JSON strings
- working with the JSON AST and cursors
- printing JSON
- semi-automatic and automatic derivation
- ADTs and configured derivation
- accumulating decoders, key codecs, and literals

## Tech Stack

- Scala `2.13.18`
- SBT
- Circe `0.14.15`
- circe-generic-extras `0.14.4`

## Repository Layout

```text
.
├── build.sbt
├── src/main/scala/Main.scala
└── src/main/scala/examples/circe
    ├── accumulatingdecoder
    ├── adt
    ├── automaticderivation
    ├── configuredderivation
    ├── cursor
    ├── customcodec
    ├── decoder
    ├── encoder
    ├── jsonast
    ├── keycodec
    ├── literal
    ├── parsing
    ├── printer
    ├── semiautoderivation
    └── support
```

Most example files define a standalone `object` with a `main` method, so they can be run directly with `sbt runMain`.

## What This Repository Contains

Examples are organized by topic under `src/main/scala/examples/circe`:

- `encoder` / `decoder`: basic codecs and helper constructors
- `customcodec`: fully manual `Encoder` / `Decoder` implementations
- `parsing`: parsing raw JSON into Circe values
- `jsonast`: creating and inspecting Circe JSON values
- `cursor`: navigating and reading nested JSON
- `printer`: formatting JSON output
- `semiautoderivation`: `deriveEncoder`, `deriveDecoder`, `deriveCodec`, and configured derivation helpers
- `automaticderivation`: automatic codec derivation
- `adt`: encoding and decoding algebraic data types
- `configuredderivation`: field-name customization with `circe-generic-extras`
- `accumulatingdecoder`: collecting multiple decoding failures
- `keycodec`: codecs for map keys
- `literal`: compile-time JSON literals
- `support`: shared output helpers used by the examples

## Running The Examples

Show the available entry points:

```bash
sbt run
```

Run a specific example:

```bash
sbt "runMain examples.circe.decoder.DecoderExamples"
```

Another example:

```bash
sbt "runMain examples.circe.semiautoderivation.SemiAutoDerivationExamples"
```
