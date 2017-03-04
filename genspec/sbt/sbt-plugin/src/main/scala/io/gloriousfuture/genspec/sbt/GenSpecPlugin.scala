package io.gloriousfuture.genspec.sbt

import sbt.Keys._
import sbt.Plugins.Basic
import sbt._
import sbt.classpath.ClasspathUtilities

import scala.language.reflectiveCalls

object GenSpecPlugin extends AutoPlugin {

  import autoImport._

  override val projectSettings: Seq[Def.Setting[_]] = {
    swag := {
      // Compile the project
      val classDirPath = (classDirectory in Compile).value
      (runMain in Compile).inputTaskValue.fullInput(classDirPath.toString)
      // Create project class loader
      val projectClassFiles = (fullClasspath in Compile).value.map(_.data)
      val loader = ClasspathUtilities.toLoader(projectClassFiles)
      val mirror = scala.reflect.runtime.universe.runtimeMirror(loader)
      val swagCls = loader.loadClass(swagRoot.value)
      val swagSymbol = mirror.moduleSymbol(swagCls)
      // Load instance of swagger generator with class loader
      val swagObj = mirror.reflectModule(swagSymbol).instance.asInstanceOf[{ def swaggerApiRootJson: String }]
      val swaggerJson = swagObj.swaggerApiRootJson
      // Print the swagger Json
      println(swaggerJson)
    }
  }

  object autoImport {

    lazy val swagRoot: SettingKey[String] = settingKey("The fully qualified class name of the specification root")

    lazy val swag: TaskKey[Unit] = taskKey("Generate REST API specs based on GenSpecPlugin configuration")
  }
}
