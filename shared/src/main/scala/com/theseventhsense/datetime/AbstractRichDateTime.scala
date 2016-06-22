package com.theseventhsense.datetime

import cats.data.Xor
import com.theseventhsense.utils.types.SSDateTime.{ DateTime, DayOfWeek, HourOfDay, TimeZone }

/**
 * Created by erik on 6/15/16.
 */
abstract class AbstractRichDateTime(dateTime: DateTime) {
  def withZone(timeZone: TimeZone): DateTime
  def withMillisOfSecond(millisOfSecond: Int): DateTime
  def withSecondOfMinute(secondOfMinute: Int): DateTime
  def withMinuteOfHour(minuteOfHour: Int): DateTime
  def withHourNumOfDay(hourOfDay: Int): DateTime
  def withHourOfDay(hourOfDay: HourOfDay): DateTime = withHourNumOfDay(hourOfDay.num)
  def withDayNumOfWeek(dayOfWeekNum: Int): DateTime
  def withDayOfWeek(dayOfWeek: DayOfWeek): DateTime
  def atStartOfDay: DateTime
  def secondOfDay: Int
  def minuteOfHour: Int
  def hourOfDay: HourOfDay
  def dayOfWeek: DayOfWeek
  def dayOfYear: Int
  def year: Int
}

abstract class AbstractRichDateTimeOps {
  def parse(s: String): Xor[DateTime.ParseError, DateTime]
}
