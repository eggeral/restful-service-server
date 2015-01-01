name := "restful-service-server"

version := "1.0"

scalaVersion := "2.11.4"

libraryDependencies ++= Seq(
  "org.rogach" %% "scallop" % "0.9.5",
  "org.glassfish.jersey.containers" % "jersey-container-grizzly2-http" % "2.14"
)

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.1" % "test",
  "org.mockito" % "mockito-core" % "1.10.17"
)