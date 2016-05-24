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
  "com.typesafe.akka" %% "akka-http-core" % "2.4.6",
  "com.typesafe.akka" %% "akka-http-spray-json-experimental" % "2.4.6",
  "com.typesafe.akka" %% "akka-slf4j" % "2.4.6",
  "com.typesafe.akka" %% "akka-actor" % "2.4.6",
  "com.typesafe" % "config" % "1.3.0"
)
