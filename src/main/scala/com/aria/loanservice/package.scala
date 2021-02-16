package com.aria

import scalaz.{-\/, \/, \/-}

import scala.concurrent.Future

package object loanservice {

  type ServiceRequestResult[T] = String \/ T
  type FutureServiceRequestResult[T] = Future[ServiceRequestResult[T]]
  val ServiceRequestResultSuccess = \/-
  val ServiceRequestResultFailure = -\/
}
