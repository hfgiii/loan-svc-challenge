package com.aria.loanservice.util

import java.text.SimpleDateFormat

import org.json4s.CustomSerializer
import org.json4s.JsonAST.JString

object LocalSerializers {

  class SimpleDateSerializer(formatter: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")) extends CustomSerializer[java.util.Date](format => ( {
    case JString(id) =>
      formatter.parse(id)
  }, {
    case x: java.util.Date =>
      JString(formatter.format(x))
  }
  ))

}
