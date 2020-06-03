package com.vs.sbt

import com.typesafe.sbt.packager.Keys.executableScriptName
import com.typesafe.sbt.packager.docker.DockerPlugin
import com.typesafe.sbt.packager.docker.DockerPlugin.autoImport._
import play.sbt.PlayScala
import sbt._
import sbt.Keys._
import sbt.plugins.JvmPlugin
import com.typesafe.sbt.packager.docker._
import com.typesafe.sbt.packager.linux.LinuxPlugin.autoImport.defaultLinuxInstallLocation

object PlayEnvironmentDependentConfigPlugin extends AutoPlugin {

  override def trigger  = allRequirements
  override def requires = JvmPlugin && PlayScala && DockerPlugin

  object autoImport {
    val environmentConfigTask = taskKey[Unit]("Changes the config.resource property file based on the environment")
  }

  import autoImport._

  override lazy val projectSettings = Seq(
    environmentConfigTask := Def.task {
      System.setProperty("config.resource",
                         System.getProperty("config.resource", s"application-${System.getenv("SCALA_ENV")}"))
    },
    compile in Compile := ((compile in Compile) dependsOn (environmentConfigTask)).value,
    dockerCommands += Cmd("RUN", "apt update && apt upgrade && apt install dumb-init"),
    dockerEntrypoint := Seq("/usr/bin/dumb-init", "--"),
    dockerCmd := Seq(
      "bash",
      "-c",
      s"exec ${(defaultLinuxInstallLocation in Docker).value}/bin/${executableScriptName.value} -Dconfig.resource=application-$${SCALA_ENV}.conf")
  )
}
