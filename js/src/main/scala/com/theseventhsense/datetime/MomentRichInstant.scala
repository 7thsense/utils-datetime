package com.theseventhsense.datetime

import cats.data.Xor
import com.theseventhsense.utils.types.SSDateTime
import com.theseventhsense.utils.types.SSDateTime.Instant.ParseError
import com.theseventhsense.utils.types.SSDateTime.{Instant, TimeZone}
import org.widok.moment.{CalendarOpts, Moment}

import scala.scalajs.js

/**
 * Created by erik on 6/15/16.
 */
class MomentRichInstant(instant: Instant) extends AbstractRichInstant(instant) with MomentImplicits {
  val DefaultHourFormat = "ha"
  val DefaultCalendarOpts = js.Dynamic.literal(
    "sameDay" -> s"[Today] $DefaultHourFormat",
    "nextDay" -> s"[Tomorrow] $DefaultHourFormat",
    "nextWeek" -> s"dddd $DefaultHourFormat",
    "lastDay" -> s"[Yesterday] $DefaultHourFormat",
    "lastWeek" -> s"[Last] dddd $DefaultHourFormat",
    "sameElse" -> s"YYYY-MM-DD $DefaultHourFormat"
  ).asInstanceOf[CalendarOpts]

  override def asIsoString: String = instant.asMoment.format

  override def calendarInZone(timeZone: TimeZone): String = {
    val moment = Moment(instant.millis)
    moment.calendar(js.undefined, DefaultCalendarOpts)
  }
}

class MomentRichInstantOps extends AbstractRichInstantOps {

  override def fromStringLocalAsUTC(s: String): Xor[ParseError, Instant] =
    Xor.left(ParseError.Unknown("not implemented"))

  override def fromString(s: String): Xor[Instant.ParseError, Instant] =
    SSDateTimeParser.parse(s).map(_.instant).leftMap(err => Instant.ParseError.Unknown(err.toString))
}
