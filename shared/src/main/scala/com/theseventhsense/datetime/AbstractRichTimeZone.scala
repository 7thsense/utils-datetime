package com.theseventhsense.datetime

import cats.data.Xor
import com.theseventhsense.utils.types.SSDateTime
import com.theseventhsense.utils.types.SSDateTime.TimeZone

/**
  * Created by erik on 6/15/16.
  */
abstract class AbstractRichTimeZone (timeZone: TimeZone){
  def valid: Boolean

  def offsetSecondsAt(instant: SSDateTime.Instant = SSDateTime.now): Integer

}

abstract class AbstractRichTimeZoneOps {
  def parse(s: String): Xor[TimeZone.ParseError, SSDateTime.TimeZone]
}
