package com.theseventhsense.datetime

import cats.data.Xor
import com.theseventhsense.utils.types.SSDateTime.{DateTime, Instant, TimeZone}
import org.widok.moment.Moment

import scala.scalajs.js.Date
import scala.util.Try

/**
 * Created by erik on 12/26/15.
 */
object SSDateTimeParser extends TSSDateTimeParser {
  override def parse(dateTimeString: String): Xor[DateTime.ParseError, DateTime] = {
    val date = Moment(dateTimeString).utc().toDate()
    val millis = date.getTime()
    val longMillis = millis.toLong
    val instant = Instant(longMillis)
    Xor.right(DateTime(instant, TimeZone.UTC))
  }
}
