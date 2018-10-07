name := "chess"

version := "0.1.0"

scalaVersion := "2.12.6"

lazy val akkaVersion = "2.5.17"
lazy val enumeratumVersion = "1.5.13"
lazy val scalaTestVersion = "3.0.5"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "com.beachape" %% "enumeratum" % enumeratumVersion,
  "org.scalatest" %% "scalatest" % scalaTestVersion % "test"
)
