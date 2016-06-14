package com.theseventhsense.utils.types

import java.util
import java.util.Date

import com.theseventhsense.datetime._
import com.theseventhsense.utils.types.SSDateTime.TimeZone.Europe.Eastern

import scala.concurrent.duration.Duration
import scala.util.Try

/**
 * Created by erik on 11/11/15.
 */

object SSDateTime {
  val ssDateTimeZoneOps: TSSDateTimeZoneOps = SSDateTimeZoneOps
  val ssDateTimeParser: TSSDateTimeParser = SSDateTimeParser
  val instantOps: AbstractInstantOps = InstantOps
  import ssDateTimeZoneOps._

  def now: Instant = Instant.now

  def parse(s: String): Instant = Instant(ssDateTimeParser.parse(s).getTime)

  case class Instant(millis: Long) extends Comparable[Instant] {
    def asDate: Date = new Date(millis)
    def isBefore(other: Instant): Boolean = millis < other.millis
    def isAfter(other: Instant): Boolean = millis > other.millis
    def +(duration: Duration): Instant = Instant(millis + duration.toMillis) //scalastyle: ignore
    def -(duration: Duration): Instant = Instant(millis - duration.toMillis) //scalastyle: ignore
    def plus(duration: Duration): Instant = Instant(millis + duration.toMillis)
    def minus(duration: Duration): Instant = Instant(millis - duration.toMillis)
    def plusMillis(m: Long): Instant = Instant(millis + m)
    def minusMillis(m: Long): Instant = Instant(millis - m)
    def plusSeconds(seconds: Long): Instant = Instant(millis + (seconds * 1000L))
    def minusSeconds(seconds: Long): Instant = Instant(millis - (seconds * 1000L))
    def plusMinutes(minutes: Int): Instant = Instant(millis + (minutes.toLong * 60L * 1000L))
    def minusMinutes(minutes: Int): Instant = Instant(millis - (minutes.toLong * 60L * 1000L))
    def plusHours(hours: Int): Instant = Instant(millis + (hours.toLong * 60L * 60L * 1000L))
    def minusHours(hours: Int): Instant = Instant(millis - (hours.toLong * 60L * 60L * 1000L))
    def plusDays(days: Int): Instant = Instant(millis + (days.toLong * 24L * 60L * 60L * 1000L))
    def minusDays(days: Int): Instant = Instant(millis - (days.toLong * 24L * 60L * 60L * 1000L))
    // TODO: use a calendar to compute plusMonths
    def plusMonths(months: Int): Instant = Instant(millis + (months.toLong * 30L * 24L * 60L * 60L * 1000L))
    def minusMonths(months: Int): Instant = Instant(millis - (months.toLong * 30L * 24L * 60L * 60L * 1000L))
    def plusYears(years: Int): Instant = plusMonths(years * 12)
    def minusYears(years: Int): Instant = plusMonths(years * 12)
    def calendarInZone(timeZone: SSDateTime.TimeZone): String =
      instantOps.instantToLongString(this, SSDateTime.TimeZone.Default)
    def calendar: String =
      this.calendarInZone(SSDateTime.TimeZone.Default)
    override def toString: String = instantAsIsoString(this)

    override def compareTo(o: Instant): Int = millis.compareTo(o.millis)
  }

  object Instant {
    implicit def ordering: Ordering[Instant] = Ordering.by { instant: Instant => instant.millis }
    def apply(date: Date): Instant = Instant(date.getTime)
    def now: Instant = apply(System.currentTimeMillis())
  }

  case class DateTime(
    instant: Instant,
    zone: TimeZone = TimeZone.UTC
  )

  object DateTime {
    def apply(millis: Long, zone: TimeZone): DateTime = DateTime(Instant(millis), zone)
  }

  trait TimeZone {
    def name: String

    def asUtilTimeZone: util.TimeZone = {
      Option(util.TimeZone.getTimeZone(name)).getOrElse(util.TimeZone.getDefault)
    }
  }

  case class CustomTimeZone(name: String) extends TimeZone {
    override def toString: String = s"$name (Custom)"
  }

  sealed trait KnownTimeZone extends TimeZone {
    def knownName: String
    override def toString: String = s"$knownName"
  }

  object KnownTimeZone {
    def from(s: String, when: SSDateTime.Instant = now): Option[TimeZone] = {
      val unknownTimeZone = KnownTimeZone.apply(s).orElse(TimeZone.parse(s))
      unknownTimeZone.flatMap { zone =>
        TimeZone.all.find(tz => offsetSeconds(tz, when) == offsetSeconds(zone, when))
      }
    }
    def apply(s: String): Option[TimeZone] = TimeZone.all.find(_.name == s)
      .orElse(TimeZone.all.find(_.knownName == s))
  }

  object TimeZone {

    case object UTC extends KnownTimeZone {
      override val name = "UTC"
      override val knownName = "Universal Time"
    }

    object US {

      case object Eastern extends KnownTimeZone {
        override val name = "America/New_York"
        override val knownName = "US/Eastern"
      }

      case object Central extends KnownTimeZone {
        val name = "America/Chicago"
        override val knownName = "US/Central"
      }

      case object Mountain extends KnownTimeZone {
        val name = "America/Denver"
        override val knownName = "US/Mountain"
      }

      case object Pacific extends KnownTimeZone {
        val name = "America/Los_Angeles"
        override val knownName = "US/Pacific"
      }

      val all = Seq(Eastern, Central, Mountain, Pacific)
    }

    object Europe {
      case object Western extends KnownTimeZone {
        val name = "Europe/Berlin"
        override val knownName = "Europe/Western"
      }
      case object Eastern extends KnownTimeZone {
        val name = "Europe/Bucharest"
        override val knownName = "Europe/Eastern"
      }
      case object Central extends KnownTimeZone {
        val name = "Europe/London"
        override val knownName = "Europe/Central"
      }

      val all = Seq(Eastern, Central, Western)

    }

    object Australia {
      case object Southern extends KnownTimeZone {
        val name = "Australia/Adelaide"
        override val knownName = "Australia/Southern"
      }

      val all = Seq(Southern)
    }

    object Pacific {
      case object Auckland extends KnownTimeZone {
        val name = "Pacific/Auckland"
        override val knownName = "Pacific/Auckland"
      }

      val all = Seq(Auckland)
    }

    val Default = US.Eastern

    val all = Seq(UTC) ++ US.all ++ Europe.all ++ Australia.all ++ Pacific.all

    def parse(s: String): Option[TimeZone] = SSDateTimeZoneOps.parse(s)

    def from(s: String): TimeZone = KnownTimeZone.apply(s).orElse(parse(s)).getOrElse(CustomTimeZone(s))
  }

  sealed trait DayOfWeek extends Product with Serializable {
    def isoNumber: Int
  }
  object DayOfWeek {
    case object Monday extends DayOfWeek { val isoNumber = 1 }
    case object Tuesday extends DayOfWeek { val isoNumber = 2 }
    case object Wednesday extends DayOfWeek { val isoNumber = 3 }
    case object Thursday extends DayOfWeek { val isoNumber = 4 }
    case object Friday extends DayOfWeek { val isoNumber = 5 }
    case object Saturday extends DayOfWeek { val isoNumber = 6 }
    case object Sunday extends DayOfWeek { val isoNumber = 7 }
    val all = Seq(Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday)
    def from(isoNumber: Int): Option[DayOfWeek] = all.find(_.isoNumber == isoNumber)
  }

  sealed trait HourOfDay extends Product with Serializable {
    def num: Int
  }

  object HourOfDay {
    case object Hour00 extends HourOfDay { val num = 0 }
    case object Hour01 extends HourOfDay { val num = 1 }
    case object Hour02 extends HourOfDay { val num = 2 }
    case object Hour03 extends HourOfDay { val num = 3 }
    case object Hour04 extends HourOfDay { val num = 4 }
    case object Hour05 extends HourOfDay { val num = 5 }
    case object Hour06 extends HourOfDay { val num = 6 }
    case object Hour07 extends HourOfDay { val num = 7 }
    case object Hour08 extends HourOfDay { val num = 8 }
    case object Hour09 extends HourOfDay { val num = 9 }
    case object Hour10 extends HourOfDay { val num = 10 }
    case object Hour11 extends HourOfDay { val num = 11 }
    case object Hour12 extends HourOfDay { val num = 12 }
    case object Hour13 extends HourOfDay { val num = 13 }
    case object Hour14 extends HourOfDay { val num = 14 }
    case object Hour15 extends HourOfDay { val num = 15 }
    case object Hour16 extends HourOfDay { val num = 16 }
    case object Hour17 extends HourOfDay { val num = 17 }
    case object Hour18 extends HourOfDay { val num = 18 }
    case object Hour19 extends HourOfDay { val num = 19 }
    case object Hour20 extends HourOfDay { val num = 20 }
    case object Hour21 extends HourOfDay { val num = 21 }
    case object Hour22 extends HourOfDay { val num = 22 }
    case object Hour23 extends HourOfDay { val num = 23 }
    val all = Seq(
      Hour00, Hour01, Hour02, Hour03, Hour04, Hour05, Hour06, Hour07, Hour08, Hour09,
      Hour10, Hour11, Hour12, Hour13, Hour14, Hour15, Hour16, Hour17, Hour18, Hour19,
      Hour20, Hour21, Hour22, Hour23
    )
    def from(num: Int): Option[HourOfDay] = all.find(_.num == num)
  }

  sealed trait DayOfMonth extends Product with Serializable {
    def num: Int
  }

  object DayOfMonth { // scalastyle:off
    case object Day01 extends DayOfMonth { val num = 1 }
    case object Day02 extends DayOfMonth { val num = 2 }
    case object Day03 extends DayOfMonth { val num = 3 }
    case object Day04 extends DayOfMonth { val num = 4 }
    case object Day05 extends DayOfMonth { val num = 5 }
    case object Day06 extends DayOfMonth { val num = 6 }
    case object Day07 extends DayOfMonth { val num = 7 }
    case object Day08 extends DayOfMonth { val num = 8 }
    case object Day09 extends DayOfMonth { val num = 9 }
    case object Day10 extends DayOfMonth { val num = 10 }
    case object Day11 extends DayOfMonth { val num = 11 }
    case object Day12 extends DayOfMonth { val num = 12 }
    case object Day13 extends DayOfMonth { val num = 13 }
    case object Day14 extends DayOfMonth { val num = 14 }
    case object Day15 extends DayOfMonth { val num = 15 }
    case object Day16 extends DayOfMonth { val num = 16 }
    case object Day17 extends DayOfMonth { val num = 17 }
    case object Day18 extends DayOfMonth { val num = 18 }
    case object Day19 extends DayOfMonth { val num = 19 }
    case object Day20 extends DayOfMonth { val num = 20 }
    case object Day21 extends DayOfMonth { val num = 21 }
    case object Day22 extends DayOfMonth { val num = 22 }
    case object Day23 extends DayOfMonth { val num = 23 }
    case object Day24 extends DayOfMonth { val num = 24 }
    case object Day25 extends DayOfMonth { val num = 25 }
    case object Day26 extends DayOfMonth { val num = 26 }
    case object Day27 extends DayOfMonth { val num = 27 }
    case object Day28 extends DayOfMonth { val num = 28 }
    case object Day29 extends DayOfMonth { val num = 29 }
    case object Day30 extends DayOfMonth { val num = 30 }
    case object Day31 extends DayOfMonth { val num = 31 }
    case object Day32 extends DayOfMonth { val num = 32 }

    val all = Seq(
      Day01, Day02, Day03, Day04, Day05, Day06, Day07, Day08, Day09,
      Day10, Day11, Day12, Day13, Day14, Day15, Day16, Day17, Day18, Day19,
      Day20, Day21, Day22, Day23, Day24, Day25, Day26, Day27, Day28, Day29,
      Day30, Day31, Day32
    )

    def from(num: Int): Option[DayOfMonth] = all.find(_.num == num)

  }

  sealed abstract class Quarter extends Product with Serializable {
    val num: Int
  }

  object Quarter {
    case object First extends Quarter { val num = 1 }
    case object Second extends Quarter { val num = 2 }
    case object Third extends Quarter { val num = 3 }
    case object Fourth extends Quarter { val num = 4 }

    val All = Seq(First, Second, Third, Fourth)
  }

  sealed trait Month extends Product with Serializable {
    val num: Int
    lazy val quarter: Quarter = Quarter.All.find(_.num == ((num - 1) / 3 + 1))
      .getOrElse(throw new RuntimeException("Invalid month"))
  }

  object Month {
    implicit val ordering: Ordering[Month] = Ordering.by(x => x.num)
    case object January extends Month { val num = 1 }
    case object February extends Month { val num = 2 }
    case object March extends Month { val num = 3 }
    case object April extends Month { val num = 4 }
    case object May extends Month { val num = 5 }
    case object June extends Month { val num = 6 }
    case object July extends Month { val num = 7 }
    case object August extends Month { val num = 8 }
    case object September extends Month { val num = 9 }
    case object October extends Month { val num = 10 }
    case object November extends Month { val num = 11 }
    case object December extends Month { val num = 12 }
    val all = Seq(January, February, March, April, May, June, July, August, September, October, November, December)
    def from(num: Int): Option[Month] = {
      all.find(_.num == num)
    }
    def from(name: String): Option[Month] = {
      all.find(_.getClass.getSimpleName.toLowerCase == name.toLowerCase)
        .orElse(all.find(_.num == name.toInt))
    }
  }

  sealed trait WeekOfMonth extends Product with Serializable {
    val num: Int
  }

  object WeekOfMonth {
    case object First extends WeekOfMonth { val num = 1 }
    case object Second extends WeekOfMonth { val num = 2 }
    case object Third extends WeekOfMonth { val num = 3 }
    case object Fourth extends WeekOfMonth { val num = 4 }
    case object Fifth extends WeekOfMonth { val num = 5 }
    val all = Seq(First, Second, Third, Fourth, Fifth)
    def from(num: Int): Option[WeekOfMonth] = all.find(_.num == num)
  }

  case class Year(year: Int) extends AnyVal {
    override def toString: String = year.toString
  }

  object Year {
    implicit val ordering: Ordering[Year] = Ordering.by(x => x.year)
  }
}
