package com.theseventhsense.datetime

import java.time.{ZoneId, ZonedDateTime}
import java.time.format.{DateTimeFormatter, DateTimeParseException, FormatStyle}

import cats.data.Xor
import com.theseventhsense.utils.types.SSDateTime.{DateTime, Instant, TimeZone}

/**
  * Created by erik on 6/15/16.
  */
class JavaTimeRichInstant(instant: Instant)
    extends AbstractRichInstant(instant) {
  def asJavaTime: java.time.Instant =
    java.time.Instant.ofEpochMilli(instant.millis)

  override def asIsoString: String =
    DateTimeFormatter.ISO_INSTANT.format(asJavaTime)

  val UTC = ZoneId.of("UTC")

  override def asCsvString: String = SSDateTimeParser.csvDateTimeFormatter.format(asJavaTime.atZone(UTC))

  override def calendarInZone(timeZone: TimeZone): String = {
    val javaInstant = java.time.Instant.ofEpochMilli(instant.millis)
    val javaZone = java.time.ZoneId.of(timeZone.name)
    val javaZonedTime =
      java.time.ZonedDateTime.ofInstant(javaInstant, javaZone)
    val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
    javaZonedTime.format(formatter)
  }
}

class JavaTimeRichInstantOps
    extends AbstractRichInstantOps
    with JavaTimeImplicits {
  val UTC = ZoneId.of("UTC")
  override def fromStringLocalAsUTC(
      s: String): Xor[Instant.ParseError, Instant] =
    Xor
      .catchNonFatal(DateTimeFormatter.ISO_LOCAL_DATE_TIME.parse(s))
      .map(java.time.LocalDateTime.from)
      .map(_.atZone(UTC).asU.instant)
      .leftMap { case err => Instant.ParseError.Unknown(err.toString) }

  def parseInstant(s: String): Xor[Instant.ParseError, Instant] =
    Xor.catchNonFatal(java.time.Instant.parse(s.replace(" ", "T"))).map(_.asU).leftMap {
      case err => Instant.ParseError.Unknown(err.toString)
    }

  def parseZoned(s: String): Xor[Instant.ParseError, Instant] =
    SSDateTimeParser.parse(s).map { case dt => dt.instant }.leftMap {
      case err => Instant.ParseError.Unknown(err.toString)
    }

  override def fromString(s: String): Xor[Instant.ParseError, Instant] =
    parseInstant(s).orElse(parseZoned(s))
}
