package com.theseventhsense.datetime

import com.theseventhsense.utils.types.SSDateTime
import org.widok.moment.{ CalendarOpts, Moment }

import scala.scalajs.js

/**
 * Created by erik on 3/19/16.
 */
object InstantOps extends AbstractInstantOps {
  val DefaultHourFormat = "ha"
  val DefaultCalendarOpts = js.Dynamic.literal(
    "sameDay" -> s"[Today] $DefaultHourFormat",
    "nextDay" -> s"[Tomorrow] $DefaultHourFormat",
    "nextWeek" -> s"dddd $DefaultHourFormat",
    "lastDay" -> s"[Yesterday] $DefaultHourFormat",
    "lastWeek" -> s"[Last] dddd $DefaultHourFormat",
    "sameElse" -> s"YYYY-MM-DD $DefaultHourFormat"
  ).asInstanceOf[CalendarOpts]

  def instantToLongString(instant: SSDateTime.Instant, timeZone: SSDateTime.TimeZone): String = {
    val moment = Moment(instant.millis)
    moment.calendar(js.undefined, DefaultCalendarOpts)
  }
}
