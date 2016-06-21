package com.theseventhsense.udatetime

import java.util.Date

import com.theseventhsense.utils.types.SSDateTime
import io.circe.generic.semiauto._
import io.circe.{ Decoder, Encoder }

/**
 * Created by erik on 6/17/16.
 */
trait UDateTimeCodecs {
  // java.util.Date and java.time codecs
  implicit lazy val dateEncoder: Encoder[Date] = {
    Encoder[Long].contramap(_.getTime)
  }
  lazy val stringDateDecoder: Decoder[Date] = {
    Decoder[String].emap(s ⇒ SSDateTime.Instant.parse(s).map(_.asDate).leftMap(_.toString))
  }
  lazy val longDateDecoder: Decoder[Date] = {
    Decoder[Long].map(millis ⇒ new Date(millis))
  }
  implicit lazy val dateDecoder: Decoder[Date] = longDateDecoder or stringDateDecoder

  //  implicit lazy val dateTimeEncoder: Encoder[DateTime] = {
  //    Encoder[Long].contramap(_.getMillis)
  //  }
  //  lazy val stringDateTimeDecoder: Decoder[DateTime] = {
  //    Decoder[String].map(DateTime.parse)
  //  }
  //  lazy val longDateTimeDecoder: Decoder[DateTime] = {
  //    Decoder[Long].map(millis ⇒ new DateTime(millis, DateTimeZone.UTC))
  //  }
  //  implicit lazy val dateTimeDecoder: Decoder[DateTime] = longDateTimeDecoder or stringDateTimeDecoder
  // DateTime codecs
  implicit lazy val dateTimeDayOfWeekEncoder: Encoder[SSDateTime.DayOfWeek] = Encoder[Int].contramap(_.isoNumber)
  implicit lazy val dateTimeDayOfWeekDecoder: Decoder[SSDateTime.DayOfWeek] = Decoder[Int]
    .map(isoNumber ⇒ SSDateTime.DayOfWeek.all.find(_.isoNumber == isoNumber).get)
  implicit lazy val dateTimeDayOfMonthEncoder: Encoder[SSDateTime.DayOfMonth] = Encoder[Int].contramap(_.num)
  implicit lazy val dateTimeDayOfMonthDecoder: Decoder[SSDateTime.DayOfMonth] = Decoder[Int]
    .map(num ⇒ SSDateTime.DayOfMonth.all.find(_.num == num).get)
  implicit lazy val dateTimeMonthEncoder: Encoder[SSDateTime.Month] = Encoder[Int].contramap(_.num)
  implicit lazy val dateTimeMonthDecoder: Decoder[SSDateTime.Month] = Decoder[Int]
    .map(num ⇒ SSDateTime.Month.all.find(_.num == num).get)
  implicit lazy val dateTimeYearEncoder: Encoder[SSDateTime.Year] = Encoder[Int].contramap(_.year)
  implicit lazy val dateTimeYearDecoder: Decoder[SSDateTime.Year] = Decoder[Int]
    .map(year ⇒ SSDateTime.Year(year))
  implicit lazy val dateTimeHourOfDayEncoder: Encoder[SSDateTime.HourOfDay] = Encoder[Int].contramap(_.num)
  implicit lazy val dateTimeHourOfDayDecoder: Decoder[SSDateTime.HourOfDay] = Decoder[Int]
    .map(num ⇒ SSDateTime.HourOfDay.all.find(_.num == num).get)
  implicit lazy val dateTimeWeekOfMonthEncoder: Encoder[SSDateTime.WeekOfMonth] = Encoder[Int].contramap(_.num)
  implicit lazy val dateTimeWeekOfMonthDecoder: Decoder[SSDateTime.WeekOfMonth] = Decoder[Int]
    .map(num ⇒ SSDateTime.WeekOfMonth.all.find(_.num == num).get)
  implicit lazy val dateTimeZoneEncoder: Encoder[SSDateTime.TimeZone] = Encoder[String].contramap(_.name)
  implicit lazy val dateTimeZoneDecoder: Decoder[SSDateTime.TimeZone] = Decoder[String].map(SSDateTime.TimeZone.from)
  implicit lazy val instantEncoder: Encoder[SSDateTime.Instant] = Encoder[Date].contramap(_.asDate)
  implicit lazy val instantDecoder: Decoder[SSDateTime.Instant] = Decoder[Date].map(SSDateTime.Instant.apply)
  implicit lazy val ssDateTimeEncoder: Encoder[SSDateTime.DateTime] = deriveEncoder[SSDateTime.DateTime]
  implicit lazy val ssDateTimeDecoder: Decoder[SSDateTime.DateTime] = deriveDecoder[SSDateTime.DateTime]
}

object UDateTimeCodecs extends UDateTimeCodecs
