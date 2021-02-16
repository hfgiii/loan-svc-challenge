package com.aria.loanservice.experiment

import scala.io.Source
import org.json4s.DefaultFormats
import org.json4s._
import org.json4s.jackson.JsonMethods._
import com.aria.loanservice.util.LocalSerializers._

import scala.util.Try

object LoanExperimentBaseLoader {

  implicit val formats  =  DefaultFormats + new SimpleDateSerializer()

  def loadLoanExperiments: Try[LoanExperimentBase] = {
     val experimentsJson = getClass.getResourceAsStream("/experiments.json")
     val lines           = Source.fromInputStream(experimentsJson).getLines()
     val experiments     = lines.foldLeft("") { case (str, jStr) => str + jStr }

     Try(parse( experiments.replaceAll("\\s{2,}", "")).extract[LoanExperimentBase].extractCurrentOffers)

  }
}
