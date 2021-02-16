package com.aria.loanservice

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.http.scaladsl.model.StatusCodes._
import akka.util.Timeout
import com.aria.loanservice.experiment.{LoanExperimentBase, LoanOfferRequest}
import org.json4s.JsonAST._
import org.json4s.jackson.{JsonMethods => jk}

import scala.concurrent.duration._
import scala.language.postfixOps

case class LoanServiceRoute(system:ActorSystem, loanExperimentBase: LoanExperimentBase) {

  implicit val execContext = system.dispatcher

  val route:Route = {

    implicit val akkaTimeout = Timeout(60 seconds)

    pathPrefix("loan-service") {
      parameterMap { params =>
        validateParams(params) match {
          case ServiceRequestResultSuccess(loanOfferRequest) => complete(findLoanOffers(loanOfferRequest))
          case ServiceRequestResultFailure(message)          => complete(NotAcceptable,message)
        }
      }
    }
  }

  private val phoneX = """(\d{3})(\d{3})(\d{7})""".r

  def validatePhoneNumber(phone:String):Boolean =
    phoneX.findAllIn(phone).nonEmpty

  def validateCreditScore(creditScore:Double):Boolean =
    creditScore >= 0.0 && creditScore <= 1.0

  def findLoanOffers(loanOfferRequest:LoanOfferRequest):String = {
    val loanOffers = loanExperimentBase.getLoanOffers(loanOfferRequest.creditScore)

    jk.compact(JObject(List("phone" -> JString(loanOfferRequest.number), "credit-score" -> JDouble(loanOfferRequest.creditScore), "loanOffers" -> JArray(loanOffers.map(_.toJSON)))))

  }

  val requestParams = Set("phone", "credit-score")

  def validateParams(params:Map[String,String]):ServiceRequestResult[LoanOfferRequest] = {
    val parmKeys = params.keySet

    if(requestParams.intersect(parmKeys) == requestParams) {
      val phone       = params("phone")
      val creditScore = params("credit-score").toDouble
      if(validatePhoneNumber(phone)) {
        if(validateCreditScore(creditScore)) {
          ServiceRequestResultSuccess(LoanOfferRequest(phone,creditScore))
        } else ServiceRequestResultFailure(s"Credit Score $creditScore is incorrect")
      } else ServiceRequestResultFailure(s"Phone Number $phone is incorrect")
    } else ServiceRequestResultFailure(s"Mismatched Parameter keys $parmKeys")

  }


}
