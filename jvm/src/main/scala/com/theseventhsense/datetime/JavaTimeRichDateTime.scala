package com.theseventhsense.datetime
import java.time.ZonedDateTime
import java.time.format.{ DateTimeFormatter, DateTimeParseException }
import java.time.temporal.{ TemporalAdjuster, TemporalAdjusters }

import cats.data.Xor
import com.theseventhsense.utils.types.SSDateTime._

/**
 * Created by erik on 6/15/16.
 */
class JavaTimeRichDateTime(dateTime: DateTime)
    extends AbstractRichDateTime(dateTime)
    with JavaTimeInstantImplicits
    with JavaTimeTimeZoneImplicits
    with JavaTimeImplicits {

  lazy val asJavaTime: ZonedDateTime = ZonedDateTime.ofInstant(dateTime.instant.asJavaTime, dateTime.zone.asJavaTime)

  override def withZone(timeZone: TimeZone): DateTime =
    asJavaTime.withZoneSameLocal(timeZone.asJavaTime).asU

  override def withMillisOfSecond(millisOfSecond: Int): DateTime =
    asJavaTime.withNano(millisOfSecond * 1000).asU

  override def withMinuteOfHour(minuteOfHour: Int): DateTime =
    asJavaTime.withMinute(minuteOfHour).asU

  override def withDayOfWeek(dayOfWeek: DayOfWeek): DateTime = {
    withDayNumOfWeek(dayOfWeek.isoNumber)
  }

  override def dayOfYear: Int = asJavaTime.getDayOfYear

  override def dayOfWeek: DayOfWeek = DayOfWeek.from(asJavaTime.getDayOfWeek.getValue).get

  override def secondOfDay: Int = asJavaTime.getSecond + minuteOfHour * 60 + hourOfDay.num * 60 * 60

  override def minuteOfHour: Int = asJavaTime.getMinute

  override def hourOfDay: HourOfDay = HourOfDay.from(asJavaTime.getHour).get

  override def withHourNumOfDay(hourOfDay: Int): DateTime = asJavaTime.withHour(hourOfDay).asU

  override def year: Int = asJavaTime.getYear

  override def atStartOfDay: DateTime = asJavaTime.toLocalDate.atStartOfDay(dateTime.zone.asJavaTime).asU

  override def withDayNumOfWeek(dayOfWeekNum: Int): DateTime = {
    val dayOfWeek = java.time.DayOfWeek.of(dayOfWeekNum)
    if (dayOfWeekNum > asJavaTime.getDayOfWeek.getValue) {
      asJavaTime.`with`(TemporalAdjusters.nextOrSame(dayOfWeek)).asU
    } else {
      asJavaTime.`with`(TemporalAdjusters.previousOrSame(dayOfWeek)).asU
    }
  }

  override def withSecondOfMinute(secondOfMinute: Int): DateTime = asJavaTime.withSecond(secondOfMinute).asU
}

class JavaTimeRichDateTimeOps extends AbstractRichDateTimeOps with JavaTimeImplicits {
  override def parse(s: String): Xor[DateTime.ParseError, DateTime] = SSDateTimeParser.parse(s)
}
