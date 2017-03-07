organization in ThisBuild := "io.gloriousfuture"
organizationName in ThisBuild := "The Glorious Future"

version in ThisBuild := "0.0.1-SNAPSHOT"
scalaVersion := "2.11.8"

licenses in ThisBuild += ("Apache-2.0", url("http://opensource.org/licenses/apache-2.0"))

lazy val root = Project("genrpc-root", file("."))
  .aggregate(
    // RPC client code generators
    `genrpc-core`,
    // TODO: Move these projects out into a "genspec" repo
    // OpenAPI spec generators
    `genspec-openapi-core`,
    `genspec-openapi-circe`,
    `genspec-openapi-play`,
    `genspec-openapi-yaml`,
    // SBT plugins
    `genspec-sbt-plugin`,
    `genspec-play-plugin`
  )
  .settings(
    publish := {},
    publishLocal := {}
  )

def commonProject(id: String, path: String): Project = {
  Project(id, file(path))
    .settings(
      name := id,

      scalacOptions ++= {
        // the deprecation:false flag is only supported by scala >= 2.11.3, but needed for scala >= 2.11.0 to avoid warnings
        (CrossVersion.partialVersion(scalaVersion.value) match {

          case Some((2, scalaMinor)) if scalaMinor >= 11 =>
            // For scala versions >= 2.11.3
            Seq("-deprecation:false", "-target:jvm-1.8", "-Ywarn-unused")

          case Some((2, scalaMinor)) if scalaMinor < 11 =>
            // For scala versions 2.10.x we can't use certain flags
            Seq.empty

        }) ++ Seq(
          "-feature",
          "-unchecked",
          "-Xfatal-warnings",
          "-Xfuture",
          "-Ywarn-value-discard"
        )
      },

      libraryDependencies ++= Seq(
        // Test-only dependencies
        Libraries.scalatest
      ).map(_ % Test)
    )
}

/**
  * The scripted sbt projects also need to know any sbt opt overrides. For example:
  * - if the .ivy2 location is in another place
  * - if logging options should be changed
  */
lazy val defaultSbtOpts = settingKey[Seq[String]]("The contents of the default_sbt_opts env var.")
lazy val javaOptsDebugger = settingKey[String]("Opens a debug port for scripted tests on 8000")

def sbtPluginProject(id: String, path: String): Project = {

  commonProject(id, path).settings(
    sbtPlugin := true,
    sbtVersion := "0.13.13",
    scalaVersion := "2.10.6",
    libraryDependencies += "org.scala-sbt" % "scripted-plugin" % sbtVersion.value
  ).settings(
    /**
      * Scripted settings (see http://eed3si9n.com/testing-sbt-plugins)
      */
    scriptedSettings,
    defaultSbtOpts := {
      sys.env.get("default_sbt_opts").toSeq ++ sys.env.get("scripted_sbt_opts")
    },
    scriptedLaunchOpts := scriptedLaunchOpts.value ++ defaultSbtOpts.value ++ Seq(
      "-Xmx1024M",
      "-Dplugin.version=" + version.value,
      javaOptsDebugger.value
    ),
    scriptedBufferLog := false,
//    javaOptsDebugger := "-agentlib:jdwp=transport=dt_socket,server=y,address=8000,suspend=n"
    javaOptsDebugger := ""
  )
}

def libProject(id: String, path: String): Project = {
  commonProject(id, path).settings(
    scalaVersion := "2.11.8"
  )
}

// Client-side libraries

lazy val `genrpc-core` = libProject("genrpc-core", "genrpc/core")

// TODO: Move to scripted
//lazy val examplePlayYaml = (project in file("example/play-yaml"))
//  .settings(commonSettings)
//  .settings(
//    name := "genrpc-example-play-yaml",
//    publish := {},
//    publishLocal := {}
//  )
//  .enablePlugins(`openapi-play`.plugins)

// Server-side libraries / plugins

/**
  * Generic code to allow converting models to OpenAPI specs.
  */
lazy val `genspec-openapi-core` = libProject("genspec-openapi-core", "genspec/openapi/core")
  .settings(
    libraryDependencies ++= Seq(
      Libraries.scalacheck,
      Libraries.scalacheckShapeless
    )
  )

/**
  * Build OpenAPI specs from circe model serializers.
  */
lazy val `genspec-openapi-circe` = libProject("genspec-openapi-circe", "genspec/openapi/circe")
  .settings(
    Plugins.macroParadise,
    libraryDependencies ++= Seq(
      Libraries.circeGeneric,
      Libraries.circeOptics,
      Libraries.circeParser
    )
  )
  .dependsOn(`genspec-openapi-core`)

/**
  * Print YAML for circe projects.
  */
lazy val `genspec-openapi-yaml` = libProject("genspec-openapi-yaml", "genspec/openapi/yaml")
  .settings(
    resolvers ++= Seq(
      Resolvers.circeYaml
    ),
    libraryDependencies ++= Seq(
      Libraries.circeYaml
    ) ++ Seq(
      // Test-only dependencies
      Libraries.scalacheckOps
    ).map(_ % Test)
  )
  .dependsOn(`genspec-openapi-core`, `genspec-openapi-circe`)

/**
  * Build OpenAPI specs from Play routes, controllers, and play model serializers.
  */
lazy val `genspec-openapi-play` = libProject("genspec-openapi-play", "genspec/openapi/play")
  .settings(
    resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
    libraryDependencies ++= Seq(
      Libraries.play25Server
    ) ++ Seq(
      Libraries.play25Test,
      Libraries.play25TestOps
    ).map(_ % Test)
  )
  .dependsOn(`genspec-openapi-core`)

// TODO: Make this generic to support all spec types using String?
/**
  * An sbt plugin that allows you to generate OpenAPI.
  */
lazy val `genspec-sbt-plugin` = sbtPluginProject("genspec-sbt-plugin", "genspec/sbt/sbt-plugin")

/**
  * An sbt plugin that hooks into play to generate the specs on reload of the application.
  */
lazy val `genspec-play-plugin` = sbtPluginProject("genspec-play-plugin", "genspec/sbt/play-plugin")
  .settings(
    resolvers += Resolvers.typesafeReleases,
    Plugins.playSbtPlugin
  )
