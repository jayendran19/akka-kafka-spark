name := "KafkaRestAPI"

version := "0.1"

scalaVersion := "2.12.7"

libraryDependencies += "org.apache.kafka" %% "kafka" % "2.1.1"

libraryDependencies += "org.apache.spark" %% "spark-core" % "2.4.0"

libraryDependencies += "org.apache.spark" %% "spark-streaming" % "2.4.0"

libraryDependencies += "org.apache.spark" %% "spark-streaming-kafka-0-10" % "2.4.0"

libraryDependencies += "com.typesafe.akka" %% "akka-http" % "10.1.8"

libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.5.23"

dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-databind" % "2.6.7"


libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.5.23"

libraryDependencies += "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.8"