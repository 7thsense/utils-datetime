package ss.utils.datetime

import java.time.ZonedDateTime
import java.time.format.{ DateTimeFormatter, TextStyle }
import java.util.Locale

import com.theseventhsense.datetime.SSDateTimeParser
import org.scalatest.{ MustMatchers, WordSpec }

class DateTimeParsersSpec extends WordSpec with MustMatchers {
  import SSDateTimeParser._

  "the DateTimeParsers.flexibleParser" when {
    val correctDateEDT = ZonedDateTime.parse("2015-08-05T09:49:33-0400", DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(Eastern))
    val correctDateEST = ZonedDateTime.from(DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(Eastern).parse("2015-01-05T09:49:33-0500"))
    val correctDateCST = ZonedDateTime.from(DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(Central).parse("2015-01-05T09:49:33-0600"))
    val correctDateMST = ZonedDateTime.from(DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(Mountain).parse("2015-01-05T09:49:33-0700"))
    val correctDatePST = ZonedDateTime.from(DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(Pacific).parse("2015-01-05T09:49:33-0800"))
    s"comparing $correctDateEST, it" should {
      "be in the EST timezone" in { correctDateEST.getZone mustEqual Eastern }
      "be set to January 5 2015, 9:49 AM and 33 seconds" in {
        correctDateEST.getYear mustEqual 2015
        correctDateEST.getMonth mustEqual 1
        correctDateEST.getDayOfMonth mustEqual 5
        correctDateEST.getHour mustEqual 9
        correctDateEST.getMinute mustEqual 49
        correctDateEST.getSecond mustEqual 33
      }
      "not be in daylight savings time" in {
        correctDateEST.getZone.getDisplayName(TextStyle.SHORT, Locale.US) mustEqual "EST"
      }
    }
    s"comparing $correctDateEDT, it" should {
      "be in the EST timezone" in { correctDateEDT.getZone mustEqual Eastern }
      "be set to August 5 2015, 9:49 AM and 33 seconds" in {
        correctDateEDT.getYear mustEqual 2015
        correctDateEDT.getMonth mustEqual 8
        correctDateEDT.getDayOfMonth mustEqual 5
        correctDateEDT.getHour mustEqual 9
        correctDateEDT.getMinute mustEqual 49
        correctDateEDT.getSecond mustEqual 33
      }
      "be in daylight savings time" in {
        correctDateEDT.getZone.getDisplayName(TextStyle.SHORT, Locale.US) mustEqual "EDT"
      }
    }
    "provided a proper iso date" should {
      val example = "2015-01-05 09:49:33 EST"
      s"be able to parse $example" in {
        parseDateTime(example) mustEqual correctDateEST
      }
    }
    "provided a date with valid timezone string" should {
      val example = "2015-01-05 09:49:33 US/Eastern"
      s"be able to parse $example" in {
        parseDateTime(example) mustEqual correctDateEST
      }
    }
    "provided a pacific date with valid timezone string" should {
      val example = "2015-01-05 09:49:33 US/Pacific"
      s"be able to parse $example" in {
        parseDateTime(example) mustEqual correctDatePST
      }
    }
  }
}
