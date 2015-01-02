organization := "software.egger"

name := "restserver"

version := "1.1.0"

scalaVersion := "2.11.4"

libraryDependencies ++= Seq(
  "org.rogach" %% "scallop" % "0.9.5",
  "org.glassfish.jersey.containers" % "jersey-container-grizzly2-http" % "2.14"
)

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.1" % "test",
  "org.mockito" % "mockito-core" % "1.10.17"
)

publishMavenStyle := true

pomIncludeRepository := { _ => false }

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

pomExtra := (
  <url>http://github.com/eggeral/restful-service-server</url>
    <licenses>
      <license>
        <name>Apache License, Version 2.0</name>
        <url>http://www.apache.org/licenses/LICENSE-2.0.html</url>
        <distribution>repo</distribution>
      </license>
    </licenses>
    <scm>
      <url>scm:git:git@github.com/eggeral/restful-service-server.git</url>
      <connection>scm:git:git@github.com/eggeral/restful-service-server.git</connection>
    </scm>
    <developers>
      <developer>
        <id>eggeral</id>
        <name>Alexander Egger</name>
        <url>http://www.egger.software</url>
      </developer>
    </developers>)