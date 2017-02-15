package com.theseventhsense.datetime

import cats.implicits._
import com.theseventhsense.utils.types.SSDateTime.DateTime

/**
  * Created by erik on 12/26/15.
  */
trait TSSDateTimeParser {
  def parse(dateTimeString: String): Either[DateTime.ParseError, DateTime]
}
