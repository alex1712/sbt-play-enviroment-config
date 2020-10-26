import sbt.internal.librarymanagement.cross.CrossVersionUtil
useCoursier := false
scalaVersion := "2.12.10"

resolvers += Resolver.sbtPluginRepo("releases")

name := """sbt-play-environment-config"""
organization := "com.vs"
version := "0.7"

sbtPlugin := true

ThisBuild / licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html"))

// ScalaTest
libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.1" % "test"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "test"

libraryDependencies += ("com.typesafe.play" % "sbt-plugin" % "2.8.2")
  .extra(("scalaVersion", CrossVersionUtil.binaryScalaVersion(scalaVersion.value)))
  .extra(("sbtVersion", CrossVersionUtil.binarySbtVersion(sbtVersion.value)))

bintrayPackageLabels := Seq("sbt","plugin")
bintrayVcsUrl := Some("""git@github.com:alex1712/sbt-play-environment-config.git""")

publishMavenStyle := false
bintrayRepository := "sbt-plugins"
bintrayOrganization in bintray := None

initialCommands in console := """import com.vs.sbt._"""

enablePlugins(ScriptedPlugin)
// set up 'scripted; sbt plugin for testing sbt plugins
scriptedLaunchOpts ++=
  Seq("-Xmx1024M", "-Dplugin.version=" + version.value)
