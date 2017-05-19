package com.theseventhsense.datetime

import com.theseventhsense.utils.types.SSDateTime.{DateTime, Instant, TimeZone}
import moment.Moment

/**
  * Created by erik on 6/15/16.
  */
trait MomentImplicits {
  implicit class RichMomentDateTime(dateTime: DateTime) {
    def asMoment = dateTime.instant.asMoment
  }
  implicit class RichMomentInstant(instant: Instant) {
    def asMoment = Moment(instant.millis)
  }
  implicit class RichMomentTimeZone(timeZone: TimeZone) {
    def asMoment = timeZone.name
  }
}

object MomentImplicits extends MomentImplicits
