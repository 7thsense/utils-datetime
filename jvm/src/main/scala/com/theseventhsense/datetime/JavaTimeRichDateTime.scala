package com.theseventhsense.datetime
import java.time.ZonedDateTime
import java.time.format.{DateTimeFormatter, DateTimeParseException}

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

  def asJavaTime: ZonedDateTime = ZonedDateTime.ofInstant(dateTime.instant.asJavaTime, dateTime.zone.asJavaTime)

  override def withZone(timeZone: TimeZone): DateTime =
    asJavaTime.withZoneSameLocal(timeZone.asJavaTime).asU

  override def withMillisOfSecond(millisOfSecond: Int): DateTime =
    asJavaTime.withNano(millisOfSecond * 1000).asU

  override def withMinuteOfHour(minuteOfHour: Int): DateTime =
    asJavaTime.withMinute(minuteOfHour).asU

  override def withDayOfWeek(dayOfWeek: DayOfWeek): DateTime =
    ???

  override def dayOfYear: Int = asJavaTime.getDayOfYear

  override def dayOfWeek: DayOfWeek = DayOfWeek.from(asJavaTime.getDayOfWeek.getValue).get

  override def hourOfDay: HourOfDay = HourOfDay.from(asJavaTime.getHour).get

  override def withHourOfDay(hourOfDay: Int): DateTime = asJavaTime.withHour(hourOfDay).asU

  override def year: Int = asJavaTime.getYear

  override def withMillisOfDay(millis: Int): DateTime = ???

  override def withDayNumOfWeek(dayOfWeekNum: Int): DateTime = ???

  override def withSecondOfMinute(secondOfMinute: Int): DateTime = asJavaTime.withSecond(secondOfMinute).asU
}

class JavaTimeRichDateTimeOps extends AbstractRichDateTimeOps with JavaTimeImplicits {
  override def parse(s: String): Xor[DateTime.ParseError, DateTime] = SSDateTimeParser.parse(s)
}
