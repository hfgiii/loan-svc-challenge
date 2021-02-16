package com.aria.loanservice.experiment
import java.util.Date
import org.json4s.JsonDSL._
import org.json4s.JsonAST._

case class LoanExperimentBase(experiments:List[LoanExperiment]) {

  def getLoanOffers(creditScore:Double):List[LoanOffer] =
    experiments.flatMap (_.matchTimelyOffersToCreditScore(creditScore))

  def extractCurrentOffers:LoanExperimentBase = {
    val now = new java.util.Date()

    LoanExperimentBase(
      experiments = experiments.filter ( ex => now.before(ex.endDate) && now.after(ex.startDate))
    )
  }

}

case class LoanExperiment(name: String, startDate: Date, endDate: Date, offers:List[LoanOffer]) {

  def matchTimelyOffersToCreditScore(creditScore:Double):List[LoanOffer] =
    if(true) offers.filter(_.minScore <= creditScore) else List.empty[LoanOffer]

}

case class LoanOffer(minScore: Double, amount: Int, fee: Int, term: Int) {
   def toJSON:JValue =
     ("minScore" -> minScore) ~
     ("amount"   -> amount)   ~
     ("fee"      -> fee)      ~
     ("term"     -> term)
}
