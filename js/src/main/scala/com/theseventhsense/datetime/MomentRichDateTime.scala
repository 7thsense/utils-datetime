package com.theseventhsense.datetime

import cats.data.Xor
import com.theseventhsense.utils.types.SSDateTime.DateTime.ParseError
import com.theseventhsense.utils.types.SSDateTime.{DateTime, DayOfWeek, HourOfDay, TimeZone}

/**
  * Created by erik on 6/15/16.
  */
class MomentRichDateTime(dateTime: DateTime) extends AbstractRichDateTime(dateTime) {
  override def withZone(timeZone: TimeZone): DateTime = ???

  override def withMillisOfSecond(millisOfSecond: Int): DateTime = ???

  override def withMinuteOfHour(minuteOfHour: Int): DateTime = ???

  override def withDayOfWeek(dayOfWeek: DayOfWeek): DateTime = ???

  override def dayOfWeek: DayOfWeek = ???

  override def dayOfYear: Int = ???

  override def hourOfDay: HourOfDay = ???

  override def withHourOfDay(hourOfDay: Int): DateTime = ???

  override def year: Int = ???

  override def withMillisOfDay(millis: Int): DateTime = ???

  override def withDayNumOfWeek(dayOfWeekNum: Int): DateTime = ???

  override def withSecondOfMinute(secondOfMinute: Int): DateTime = ???
}

class MomentRichDateTimeOps extends AbstractRichDateTimeOps {
  override def parse(s: String): Xor[ParseError, DateTime] = SSDateTimeParser.parse(s)
}
