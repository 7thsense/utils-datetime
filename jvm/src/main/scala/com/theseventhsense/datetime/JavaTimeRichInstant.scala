package com.theseventhsense.datetime

import java.time.ZonedDateTime
import java.time.format.{DateTimeFormatter, DateTimeParseException, FormatStyle}

import cats.data.Xor
import com.theseventhsense.utils.types.SSDateTime.{DateTime, Instant, TimeZone}

/**
  * Created by erik on 6/15/16.
  */
class JavaTimeRichInstant(instant: Instant) extends AbstractRichInstant(instant) {
  def asJavaTime: java.time.Instant = java.time.Instant.ofEpochMilli(instant.millis)

  override def asIsoString: String = DateTimeFormatter.ISO_INSTANT.format(asJavaTime)

  override def calendarInZone(timeZone: TimeZone): String = {
    val javaInstant   = java.time.Instant.ofEpochMilli(instant.millis)
    val javaZone      = java.time.ZoneId.of(timeZone.name)
    val javaZonedTime = java.time.ZonedDateTime.ofInstant(javaInstant, javaZone)
    val formatter     = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
    javaZonedTime.format(formatter)
  }
}

class JavaTimeRichInstantOps extends AbstractRichInstantOps with JavaTimeImplicits {
  override def fromString(s: String): Xor[Instant.ParseError, Instant] =
    SSDateTimeParser
      .parse(s)
      .bimap({ case err => Instant.ParseError.Unknown(err.toString) }, { case dt => dt.instant })
}
