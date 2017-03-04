package io.gloriousfuture.genspec.sbt.play

import java.net.InetSocketAddress

import play.PlayRunHook
import sbt.classpath.ClasspathUtilities

case class GenSpecPlayHook(rootSpecGenerator: String) extends PlayRunHook {
  override def beforeStarted(): Unit = {
    println(s"Hook beforeStarted (appclasspath=${ClasspathUtilities.AppClassPath}, bootclasspath=${ClasspathUtilities.BootClassPath})")
  }

  override def afterStarted(addr: InetSocketAddress): Unit = {
    println(s"Hook afterStarted addr=$addr")
  }

  override def afterStopped(): Unit = {
    println("Hook afterStopped")
  }

  override def onError(): Unit = {
    println("Hook onError")
  }
}
