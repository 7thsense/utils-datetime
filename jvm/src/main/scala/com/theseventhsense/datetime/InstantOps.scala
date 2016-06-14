package com.theseventhsense.datetime
import java.time.format.{ DateTimeFormatter, FormatStyle }
import java.time.{ Instant, ZoneId }

import com.theseventhsense.utils.types.SSDateTime

/**
 * Created by erik on 3/19/16.
 */
object InstantOps extends AbstractInstantOps {
  override def instantToLongString(instant: SSDateTime.Instant, timeZone: SSDateTime.TimeZone): String = {
    val javaInstant = Instant.ofEpochMilli(instant.millis)
    val javaZone = ZoneId.of(timeZone.name)
    val javaZonedTime = java.time.ZonedDateTime.ofInstant(javaInstant, javaZone)
    val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
    javaZonedTime.format(formatter)
  }
}
