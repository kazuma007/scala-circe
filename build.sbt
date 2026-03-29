ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.18"

lazy val circeVersion = "0.14.15"
lazy val circeGenericExtrasVersion = "0.14.4"

lazy val root = (project in file("."))
  .settings(
    name := "scala-circe",
    scalacOptions += "-Ymacro-annotations",
    libraryDependencies ++= Seq(
      "io.circe" %% "circe-core" % circeVersion,
      "io.circe" %% "circe-parser" % circeVersion,
      "io.circe" %% "circe-generic" % circeVersion,
      "io.circe" %% "circe-generic-extras" % circeGenericExtrasVersion,
      "io.circe" %% "circe-literal" % circeVersion,
      "io.circe" %% "circe-pointer" % circeVersion,
      "io.circe" %% "circe-pointer-literal" % circeVersion,
      "io.circe" %% "circe-shapes" % circeVersion
    )
  )
