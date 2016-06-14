package com.theseventhsense.datetime

import com.theseventhsense.utils.types.SSDateTime
import com.theseventhsense.utils.types.SSDateTime.Instant
import org.widok.moment.Moment

/**
 * Created by erik on 2/18/16.
 */
object SSDateTimeZoneOps extends TSSDateTimeZoneOps {
  override def isValid(s: String): Boolean = true

  override def offsetSeconds(zone: SSDateTime.TimeZone, instant: SSDateTime.Instant): Integer = 0

  override def parse(s: String): Option[SSDateTime.TimeZone] = None

  override def instantAsIsoString(instant: Instant): String = {
    Moment(instant.millis).utc().format()
  }
}
