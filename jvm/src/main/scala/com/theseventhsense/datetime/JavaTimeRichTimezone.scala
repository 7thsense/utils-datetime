package com.theseventhsense.datetime

import java.time.ZoneId

import cats.data.Xor
import com.theseventhsense.utils.types.SSDateTime
import com.theseventhsense.utils.types.SSDateTime.{ Instant, TimeZone }

import scala.util.Try

/**
 * Created by erik on 6/15/16.
 */
class JavaTimeRichTimeZone(timeZone: TimeZone)
    extends AbstractRichTimeZone(timeZone)
    with JavaTimeInstantImplicits {
  override def valid: Boolean = Try(ZoneId.of(timeZone.name)).isSuccess

  override def offsetSecondsAt(instant: Instant): Integer =
    ZoneId.of(timeZone.name).getRules.getOffset(instant.asJavaTime).getTotalSeconds
}

class JavaTimeRichTimeZoneOps extends AbstractRichTimeZoneOps {
  class JavaTimeZone(id: ZoneId) extends SSDateTime.TimeZone {
    def name: String = id.getId
  }

  def normalizeOffset(offset: String): Xor[NumberFormatException, String] = {
    Xor.catchOnly[NumberFormatException](Integer.parseInt(offset)).map(o => "%+05d".format(o))
  }

  override def parse(s: String): Xor[SSDateTime.TimeZone.ParseError, SSDateTime.TimeZone] = {
    Xor
      .catchNonFatal(ZoneId.of(s))
      .orElse(normalizeOffset(s).flatMap(offset => Xor.catchNonFatal(ZoneId.of(offset))))
      .leftMap { ex =>
        TimeZone.ParseError.Unknown
      }
      .map(id => new JavaTimeZone(id))
  }
}
