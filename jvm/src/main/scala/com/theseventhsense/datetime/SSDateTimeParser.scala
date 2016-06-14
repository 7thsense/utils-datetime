package com.theseventhsense.datetime

import java.util.Date

import org.joda.time.{ DateTime, DateTimeZone }
import org.joda.time.format.{ ISODateTimeFormat, DateTimeFormatterBuilder, DateTimeFormat, DateTimeFormatter }

object SSDateTimeParser extends TSSDateTimeParser {
  // Force the default timezone to be UTC
  DateTimeZone.setDefault(DateTimeZone.UTC)

  lazy val Eastern: DateTimeZone = DateTimeZone.forID("US/Eastern")
  lazy val Central: DateTimeZone = DateTimeZone.forID("US/Central")
  lazy val Mountain: DateTimeZone = DateTimeZone.forID("US/Mountain")
  lazy val Pacific: DateTimeZone = DateTimeZone.forID("US/Pacific")

  lazy val noDateSeperatorsDateTimeFormatter: DateTimeFormatter = DateTimeFormat.forPattern("yyyyMMddHH:mm:ss ZZZ")

  lazy val dateTimeFormatterWithTimeZone: DateTimeFormatter = DateTimeFormat.forPattern("yyyyMMdd HH:mm:ss ZZZ")

  lazy val spacesDateTimeFormatterWithTimeZone: DateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss ZZZ")

  lazy val flexibleFormatter: DateTimeFormatter = {
    val builder: DateTimeFormatterBuilder = new DateTimeFormatterBuilder()
    val parsers: Array[org.joda.time.format.DateTimeParser] = Array(
      ISODateTimeFormat.dateTimeParser.withOffsetParsed.getParser,
      noDateSeperatorsDateTimeFormatter.getParser,
      dateTimeFormatterWithTimeZone.getParser,
      spacesDateTimeFormatterWithTimeZone.getParser
    )
    builder.append(None.orNull, parsers).toFormatter
  }

  val timeZoneAbbreviations: Map[String, DateTimeZone] = Map(
    "EST" -> Eastern,
    "EDT" -> Eastern,
    "CST" -> Central,
    "CDT" -> Central,
    "MST" -> Mountain,
    "MDT" -> Mountain,
    "PST" -> Pacific,
    "PDT" -> Pacific
  )

  def parseDateTime(dateTimeString: String): DateTime = {
    if (Option(dateTimeString).isEmpty || dateTimeString == "" || dateTimeString == "null") {
      new DateTime(0L)
    } else if (isAllDigits(dateTimeString)) {
      parseDateTime(dateTimeString.toLong)
    } else {
      var dts = dateTimeString
      for ((abbr, zone) <- timeZoneAbbreviations.toSeq) {
        dts = dts.replace(abbr, zone.getID)
      }
      flexibleFormatter.parseDateTime(dts)
    }
  }

  def parseDateTime(number: Long): DateTime = {
    new DateTime(number, DateTimeZone.UTC)
  }

  def isAllDigits(x: String): Boolean = x forall Character.isDigit

  override def parse(dateTimeString: String): Date = parseDateTime(dateTimeString).toDate
}
