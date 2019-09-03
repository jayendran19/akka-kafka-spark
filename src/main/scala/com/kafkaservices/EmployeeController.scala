package com.kafkaservices

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import com.kafkaservices.EmployeeController.Employee
import spray.json.DefaultJsonProtocol

import scala.util.{Failure, Success}

object EmployeeController {

  case class Employee(id: String,
                      firstName: String,
                      lastName: String)

  object EmployeeJsonProtocol extends SprayJsonSupport with DefaultJsonProtocol {


    implicit val employeeFormat = jsonFormat3(Employee.apply)
  }

}


trait EmployeeController {


  /**
    * The Actor system to be used by the Future Context.
    *
    * @return
    */
  implicit def actorSystem: ActorSystem

  import com.kafkaservices.EmployeeController.EmployeeJsonProtocol._

  /**
    * Logging using the actor system.
    */
  lazy val logger = Logging(actorSystem, classOf[EmployeeController])
  /**
    * Employee Routes for the GET/POST/Other REST endpoints for the Employee endpoints.
    */
  lazy val employeeRoutes: Route = pathPrefix("publish") {
    post {
      path("query") {
        entity(as[Employee]) { q =>
          onComplete(prod.produce(q)) {
            _ match {
              case Success(mesg) =>
                logger.info("Got the employee topic for the publish record.")
                complete(StatusCodes.OK, "Message Published Sucessfully !!! And the Message Count is "+ mesg)
              case Failure(throwable) =>
                logger.error("Failed to Publish the employee.")
                complete(StatusCodes.InternalServerError, "Failed to Publish the employees.")
            }
          }
        }
      }
    }
  }
  val prod = new Producer
}
