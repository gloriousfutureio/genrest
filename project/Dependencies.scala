import sbt._

object Versions {

  final val circe = "0.6.0"
  final val circeYaml = "0.3.0"
  final val monocle = "1.3.2"
  final val play25 = "2.5.12"
  final val playTestOps = "0.2.2"
  final val scalacheckShapeless = "1.1.3"
  final val scalacheck = "1.13.4"
  final val scalacheckOps = "1.5.0"
  final val scalatest = "3.0.0"
}

object Libraries {

  val circeCore: ModuleID = "io.circe" %% "circe-core" % Versions.circe
  val circeGeneric: ModuleID = "io.circe" %% "circe-generic" % Versions.circe
  val circeOptics: ModuleID = "io.circe" %% "circe-optics" % Versions.circe
  val circeParser: ModuleID = "io.circe" %% "circe-parser" % Versions.circe
  val circeYaml: ModuleID = "io.github.jeremyrsmith" %% "circe-yaml" % Versions.circeYaml
  val monocleCore: ModuleID = "com.github.julien-truffaut" %% "monocle-core" % Versions.monocle
  val monocleMacro: ModuleID = "com.github.julien-truffaut" %% "monocle-macro" % Versions.monocle
  val play25Server: ModuleID = "com.typesafe.play" %% "play" % Versions.play25
  val play25Test: ModuleID = "com.typesafe.play" %% "play-test" % Versions.play25
  val play25TestOps: ModuleID = "me.jeffmay" %% "play25-test-ops-core" % Versions.playTestOps
  val scalacheckShapeless: ModuleID = "com.github.alexarchambault" %% "scalacheck-shapeless_1.13" % Versions.scalacheckShapeless
  val scalacheck: ModuleID = "org.scalacheck" %% "scalacheck" % Versions.scalacheck
  val scalacheckOps: ModuleID = "me.jeffmay" %% "scalacheck-ops_1-13" % Versions.scalacheckOps
  val scalatest: ModuleID = "org.scalatest" %% "scalatest" % Versions.scalatest
}

object Plugins {

  val macroParadise: Setting[Seq[ModuleID]] = addCompilerPlugin("org.scalamacros" %% "paradise" % "2.1.0" cross CrossVersion.full)
  val playSbtPlugin: Setting[Seq[ModuleID]] = addSbtPlugin("com.typesafe.play" % "sbt-plugin" % Versions.play25)
}

object Resolvers {

  val circeYaml: MavenRepository = Resolver.bintrayRepo("jeremyrsmith", "maven")
  val typesafeReleases: MavenRepository = Resolver.typesafeRepo("releases")
}

