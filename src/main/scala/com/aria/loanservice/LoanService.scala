package com.aria.loanservice

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.aria.loanservice.experiment.LoanExperimentBaseLoader
import scala.util._
import scala.concurrent.ExecutionContextExecutor

object LoanService extends App {

  implicit val system: ActorSystem = ActorSystem("aria-loan-service")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  val experimentBaseTried = LoanExperimentBaseLoader.loadLoanExperiments
  experimentBaseTried match {
    case Success(experimentBase) =>

      val binding = Http().bindAndHandle(LoanServiceRoute(system,experimentBase).route, "0.0.0.0", 8081)

      system.registerOnTermination{
        binding.foreach(_.unbind())
      }

    case Failure(ex) => println(s"Failed Reading Experiments File with exception $ex and message ${ex.getLocalizedMessage}")
  }

}
