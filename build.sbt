lazy val commonSettings = Seq(
  organization := "com.nickmcavoy",
  version := "0.0.1-SNAPSHOT`",
  scalaVersion := "2.11.8"
)

lazy val core = project.
  settings(commonSettings: _*).
  settings(
    name := "trellol.core"
  )

lazy val timekeeping = project.dependsOn(core).
  settings(commonSettings: _*).
  settings(
    name := "trellol.timekeeping"
  )
lazy val guestlist = project.dependsOn(core).
  settings(commonSettings: _*).
  settings(
    name := "trellol.guestlist"
  )

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "trellol"
  ).
  aggregate(core, timekeeping, guestlist)

libraryDependencies in Global ++= List(
  "com.typesafe.akka"      %% "akka-http-core"                    % "2.4.6",
  "com.typesafe.akka"      %% "akka-http-spray-json-experimental" % "2.4.6",
  "com.typesafe.akka"      %% "akka-slf4j"                        % "2.4.6",
  "com.typesafe.akka"      %% "akka-actor"                        % "2.4.6",
  "org.scala-lang.modules" %% "scala-parser-combinators"          % "1.0.4",
  "com.typesafe"           %  "config"                            % "1.3.0",
  "org.scalactic"          %% "scalactic"                         % "2.2.6",
  "org.scalatest"          %% "scalatest"                         % "2.2.6" % "test"
)
scalacOptions in Global := Seq("-feature", "-deprecation", "-language:implicitConversions")
