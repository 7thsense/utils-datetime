package com.theseventhsense.datetime

import com.theseventhsense.utils.types.SSDateTime
import com.theseventhsense.utils.types.SSDateTime.TimeZone

import scala.util.Try

/**
 * Created by erik on 2/18/16.
 */
trait TSSDateTimeZoneOps {
  def isValid(s: String): Boolean
  def offsetSeconds(zone: SSDateTime.TimeZone, instant: SSDateTime.Instant): Integer
  def parse(s: String): Option[SSDateTime.TimeZone]
  def instantAsIsoString(instant: SSDateTime.Instant): String

  implicit class RichTimeZone(t: TimeZone) {
    def valid: Boolean = isValid(t.name)

    def offsetSecondsAt(instant: SSDateTime.Instant = SSDateTime.now): Integer = offsetSeconds(t, instant)
  }

  implicit class RichInstant(instant: SSDateTime.Instant) {
    def asIsoString: String = instantAsIsoString(instant)
  }

  def normalizeOffset(offset: String): Option[String] = {
    Try(Integer.parseInt(offset)).toOption.map(o => "%+05d".format(o))
  }

}
