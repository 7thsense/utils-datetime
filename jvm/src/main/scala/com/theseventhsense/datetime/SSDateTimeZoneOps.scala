package com.theseventhsense.datetime

import java.time.format.DateTimeFormatter
import java.time.{ Instant, ZoneId }

import com.theseventhsense.utils.types.SSDateTime
import com.theseventhsense.utils.types.SSDateTime.Instant

import scala.util.Try

/**
 * Created by erik on 2/18/16.
 */
object SSDateTimeZoneOps extends TSSDateTimeZoneOps {
  implicit class JavaTimeInstantOps(instant: SSDateTime.Instant) {
    def toJava: java.time.Instant = java.time.Instant.ofEpochMilli(instant.millis)
  }

  class JavaTimeZone(id: ZoneId) extends SSDateTime.TimeZone {
    def name: String = id.getId
  }

  override def isValid(s: String): Boolean = Try(ZoneId.of(s)).isSuccess

  override def offsetSeconds(zone: SSDateTime.TimeZone, instant: SSDateTime.Instant): Integer = {
    ZoneId.of(zone.name).getRules.getOffset(instant.toJava).getTotalSeconds
  }

  override def parse(s: String): Option[SSDateTime.TimeZone] = {
    Try(ZoneId.of(s)).toOption
      .orElse(normalizeOffset(s).flatMap(offset => Try(ZoneId.of(offset)).toOption))
      .map(id => new JavaTimeZone(id))
  }

  override def instantAsIsoString(instant: SSDateTime.Instant): String = {
    DateTimeFormatter.ISO_INSTANT.format(instant.toJava)
  }
}
