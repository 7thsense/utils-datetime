package com.theseventhsense.udatetime

import java.util.Date

import com.theseventhsense.utils.types.SSDateTime
import com.theseventhsense.utils.types.SSDateTime.{ DayOfWeek, HourOfDay }
import io.circe.generic.semiauto._
import io.circe.{ Decoder, Encoder, KeyDecoder, KeyEncoder }

import scala.util.Try

/**
 * Created by erik on 6/17/16.
 */
trait UDateTimeCodecs {
  // java.util.Date and java.time codecs
  implicit val dateEncoder: Encoder[Date] = {
    Encoder[Long].contramap(_.getTime)
  }
  val stringDateDecoder: Decoder[Date] = {
    Decoder[String].emap(s ⇒ SSDateTime.Instant.parse(s).map(_.asDate).leftMap(_.toString))
  }
  val longDateDecoder: Decoder[Date] = {
    Decoder[Long].map(millis ⇒ new Date(millis))
  }
  implicit val dateDecoder: Decoder[Date] = longDateDecoder or stringDateDecoder

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
  implicit val dateTimeDayOfWeekEncoder: Encoder[SSDateTime.DayOfWeek] = Encoder[Int].contramap(_.isoNumber)
  implicit val dateTimeDayOfWeekDecoder: Decoder[SSDateTime.DayOfWeek] = Decoder[Int]
    .map(isoNumber ⇒ SSDateTime.DayOfWeek.all.find(_.isoNumber == isoNumber).get)
  implicit val dateTimeDayOfMonthEncoder: Encoder[SSDateTime.DayOfMonth] = Encoder[Int].contramap(_.num)
  implicit val dateTimeDayOfMonthDecoder: Decoder[SSDateTime.DayOfMonth] = Decoder[Int]
    .map(num ⇒ SSDateTime.DayOfMonth.all.find(_.num == num).get)
  implicit val dateTimeMonthEncoder: Encoder[SSDateTime.Month] = Encoder[Int].contramap(_.num)
  implicit val dateTimeMonthDecoder: Decoder[SSDateTime.Month] = Decoder[Int]
    .map(num ⇒ SSDateTime.Month.all.find(_.num == num).get)
  implicit val dateTimeYearEncoder: Encoder[SSDateTime.Year] = Encoder[Int].contramap(_.year)
  implicit val dateTimeYearDecoder: Decoder[SSDateTime.Year] = Decoder[Int]
    .map(year ⇒ SSDateTime.Year(year))
  implicit val dateTimeHourOfDayEncoder: Encoder[SSDateTime.HourOfDay] = Encoder[Int].contramap(_.num)
  implicit val dateTimeHourOfDayDecoder: Decoder[SSDateTime.HourOfDay] = Decoder[Int]
    .map(num ⇒ SSDateTime.HourOfDay.all.find(_.num == num).get)
  implicit val dateTimeWeekOfMonthEncoder: Encoder[SSDateTime.WeekOfMonth] = Encoder[Int].contramap(_.num)
  implicit val dateTimeWeekOfMonthDecoder: Decoder[SSDateTime.WeekOfMonth] = Decoder[Int]
    .map(num ⇒ SSDateTime.WeekOfMonth.all.find(_.num == num).get)
  implicit val dateTimeZoneEncoder: Encoder[SSDateTime.TimeZone] = Encoder[String].contramap(_.name)
  implicit val dateTimeZoneDecoder: Decoder[SSDateTime.TimeZone] = Decoder[String].map(SSDateTime.TimeZone.from)
  implicit val instantEncoder: Encoder[SSDateTime.Instant] = Encoder[Date].contramap(_.asDate)
  implicit val instantDecoder: Decoder[SSDateTime.Instant] = Decoder[Date].map(SSDateTime.Instant.apply)
  implicit val ssDateTimeEncoder: Encoder[SSDateTime.DateTime] = deriveEncoder[SSDateTime.DateTime]
  implicit val ssDateTimeDecoder: Decoder[SSDateTime.DateTime] = deriveDecoder[SSDateTime.DateTime]

  implicit val yearKeyEncoder = new KeyEncoder[SSDateTime.Year] {
    override def apply(key: SSDateTime.Year): String = key.year.toString
  }
  implicit val yearKeyDecoder = new KeyDecoder[SSDateTime.Year] {
    override def apply(key: String): Option[SSDateTime.Year] =
      Try(key.toInt).toOption.map(year => SSDateTime.Year(year))
  }
  implicit val quarterKeyEncoder = new KeyEncoder[SSDateTime.Quarter] {
    override def apply(key: SSDateTime.Quarter): String = key.num.toString
  }
  implicit val quarterKeyDecoder = new KeyDecoder[SSDateTime.Quarter] {
    override def apply(key: String): Option[SSDateTime.Quarter] = SSDateTime.Quarter.fromString(key)
  }
  implicit val monthKeyEncoder = new KeyEncoder[SSDateTime.Month] {
    override def apply(key: SSDateTime.Month): String = key.num.toString
  }
  implicit val monthKeyDecoder = new KeyDecoder[SSDateTime.Month] {
    override def apply(key: String): Option[SSDateTime.Month] = SSDateTime.Month.fromString(key)
  }
  implicit val dayOfWeekKeyEncoder = new KeyEncoder[DayOfWeek] {
    override def apply(key: DayOfWeek): String = key.isoNumber.toString
  }
  implicit val dayOfWeekKeyDecoder = new KeyDecoder[DayOfWeek] {
    override def apply(key: String): Option[DayOfWeek] = DayOfWeek.fromString(key)
  }
  implicit val hourOfDayKeyEncoder = new KeyEncoder[HourOfDay] {
    override def apply(key: HourOfDay): String = key.num.toString
  }
  implicit val hourOfDayDecoder = new KeyDecoder[HourOfDay] {
    override def apply(key: String): Option[HourOfDay] = HourOfDay.fromString(key)
  }

}

object UDateTimeCodecs extends UDateTimeCodecs
