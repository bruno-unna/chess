name := "chess"

version := "0.1.0"

scalaVersion := "2.13.0"

lazy val akkaVersion = "2.5.23"
lazy val scalaTestVersion = "3.1.0-SNAP13"
lazy val enumeratumVersion = "1.5.13"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "org.scalatest" %% "scalatest" % scalaTestVersion,
  "com.beachape" %% "enumeratum" % enumeratumVersion,
)

enablePlugins(JavaAppPackaging)
