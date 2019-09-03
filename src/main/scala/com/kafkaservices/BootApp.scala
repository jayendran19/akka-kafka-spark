package com.kafkaservices

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object BootApp extends App with EmployeeController {

  var consumerthread = new Consumer
  //consumerthread.start()
  implicit val actorSystem = ActorSystem("AkkaHTTPExampleServices")
  implicit val materializer = ActorMaterializer()

  lazy val apiRoutes: Route = pathPrefix("kafka") {
    employeeRoutes
  }

  Http().bindAndHandle(apiRoutes, "localhost", 8083)
  logger.info("Starting the HTTP server at 8083")
  Await.result(actorSystem.whenTerminated, Duration.Inf)


}
