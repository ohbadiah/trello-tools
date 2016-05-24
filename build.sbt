lazy val commonSettings = Seq(
  organization := "com.nickmcavoy",
  version := "0.0.1-SNAPSHOT`",
  scalaVersion := "2.11.8"
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "trellol"
)

libraryDependencies ++= List(
//  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "com.typesafe.akka" %% "akka-http-core" % "2.4.6",
  "com.typesafe.akka" %% "akka-http-spray-json-experimental" % "2.4.6",
  "com.typesafe.akka" %% "akka-slf4j" % "2.4.6",
  "com.typesafe.akka" %% "akka-actor" % "2.4.6",
  "com.typesafe" % "config" % "1.3.0"

//  "org.scalatest" %% "scalatest" % "2.0" % "test"
)
