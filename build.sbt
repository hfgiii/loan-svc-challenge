name := "aria-loan-service-challenge"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.25",
  "com.typesafe.akka" %% "akka-stream" % "2.5.25",
  "com.typesafe.akka" %% "akka-http" % "10.1.8",
  "org.json4s" %% "json4s-native"  % "3.5.3",
  "org.json4s" %% "json4s-jackson" % "3.5.3",
  "org.scalaz"  %% "scalaz-core" % "7.2.16"
)


