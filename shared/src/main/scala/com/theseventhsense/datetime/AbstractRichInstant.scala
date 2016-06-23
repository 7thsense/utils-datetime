package com.theseventhsense.datetime

import cats.data.Xor
import com.theseventhsense.utils.types.SSDateTime.{ Instant, TimeZone }

/**
 * Created by erik on 6/15/16.
 */
abstract class AbstractRichInstant(instant: Instant) {
  def asIsoString: String
  def calendarInZone(timeZone: TimeZone): String
}

abstract class AbstractRichInstantOps {
  def fromLong(s: String) = Xor.catchNonFatal(s.toLong)
    .map(millis => Instant(millis))
    .leftMap(ex => Instant.ParseError.Unknown(ex.getMessage))

  def fromString(s: String): Xor[Instant.ParseError, Instant]

  def fromStringLocalAsUTC(s: String): Xor[Instant.ParseError, Instant]

  def parse(s: String): Xor[Instant.ParseError, Instant] = fromLong(s).orElse(fromString(s))

  def parseLocalAsUTC(s: String): Xor[Instant.ParseError, Instant] =
    fromString(s)
      .orElse(fromStringLocalAsUTC(s))
}
