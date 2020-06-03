package com.vs.sbt

import play.sbt.PlayScala
import sbt._
import sbt.Keys._
import sbt.plugins.JvmPlugin

object PlayEnvironmentDependentConfigPlugin extends AutoPlugin {

  override def trigger  = allRequirements
  override def requires = JvmPlugin && PlayScala

  object autoImport {
    val environmentConfigTask = taskKey[Unit]("Changes the config.resource property file based on the environment")
  }

  import autoImport._

  override lazy val projectSettings = Seq(
    environmentConfigTask := Def.task {
      System.setProperty("config.resource",
                         System.getProperty("config.resource", s"application-${System.getenv("SCALA_ENV")}"))
    },
    compile in Compile := ((compile in Compile) dependsOn (environmentConfigTask)).value
  )
}
