package com.theseventhsense.datetime

import java.time.format.{ DateTimeFormatter, DateTimeFormatterBuilder }
import java.time.{ ZoneId, ZonedDateTime }

import cats.data.Xor
import com.theseventhsense.utils.types.SSDateTime.DateTime

object SSDateTimeParser extends TSSDateTimeParser with JavaTimeImplicits {
  // Force the default timezone to be UTC

  lazy val Eastern = ZoneId.of("US/Eastern")
  lazy val Central = ZoneId.of("US/Central")
  lazy val Mountain = ZoneId.of("US/Mountain")
  lazy val Pacific = ZoneId.of("US/Pacific")

  lazy val noDateSeperatorsDateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHH:mm:ss ZZZ")

  lazy val noOffsetSeperatorDateTimeFormatter1: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX")

  lazy val noOffsetSeperatorDateTimeFormatter2: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXX")

  lazy val noOffsetSeperatorDateTimeFormatter3: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")

  lazy val dateTimeFormatterWithTimeZone: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss ZZZ")

  lazy val spacesDateTimeFormatterWithTimeZone: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss ZZZ")

  lazy val formatters: Seq[DateTimeFormatter] = Seq(
    DateTimeFormatter.ISO_OFFSET_DATE_TIME,
    DateTimeFormatter.ISO_ZONED_DATE_TIME,
    DateTimeFormatter.ISO_INSTANT,
    DateTimeFormatter.RFC_1123_DATE_TIME,
    noOffsetSeperatorDateTimeFormatter1,
    noOffsetSeperatorDateTimeFormatter2,
    noOffsetSeperatorDateTimeFormatter3,
    noDateSeperatorsDateTimeFormatter,
    dateTimeFormatterWithTimeZone,
    spacesDateTimeFormatterWithTimeZone
  )

  lazy val flexibleFormatter: DateTimeFormatter = {
    val builder = new DateTimeFormatterBuilder()
    formatters.foreach(builder.appendOptional)
    builder.toFormatter
  }

  val timeZoneAbbreviations: Map[String, ZoneId] = Map(
    "EST" -> Eastern,
    "EDT" -> Eastern,
    "CST" -> Central,
    "CDT" -> Central,
    "MST" -> Mountain,
    "MDT" -> Mountain,
    "PST" -> Pacific,
    "PDT" -> Pacific
  )

  def parseDateTime(dateTimeString: String): Xor[DateTime.ParseError, ZonedDateTime] = {
    if (Option(dateTimeString).isEmpty || dateTimeString == "" || dateTimeString == "null") {
      Xor.right(ZonedDateTime.ofInstant(java.time.Instant.ofEpochMilli(0L), ZoneId.of("UTC")))
    } else if (isAllDigits(dateTimeString)) {
      Xor.right(parseDateTime(dateTimeString.toLong))
    } else {
      var dts = dateTimeString
      for ((abbr, zone) <- timeZoneAbbreviations.toSeq) {
        dts = dts.replace(abbr, zone.getId)
      }
      Xor.catchNonFatal(ZonedDateTime.parse(dts, flexibleFormatter))
        .leftMap(ex => DateTime.ParseError.Unknown(ex.getMessage))
    }
  }

  def parseDateTime(number: Long): ZonedDateTime = {
    ZonedDateTime.ofInstant(java.time.Instant.ofEpochMilli(number), ZoneId.of("UTC"))
  }

  def isAllDigits(x: String): Boolean = x forall Character.isDigit

  override def parse(dateTimeString: String): Xor[DateTime.ParseError, DateTime] =
    parseDateTime(dateTimeString).map(_.asU)
}
