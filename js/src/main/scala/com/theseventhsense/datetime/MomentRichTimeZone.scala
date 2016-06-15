package com.theseventhsense.datetime

import cats.data.Xor
import com.theseventhsense.utils.types.SSDateTime.{Instant, TimeZone}

/**
  * Created by erik on 6/15/16.
  */
class MomentRichTimeZone(timeZone: TimeZone) extends AbstractRichTimeZone(timeZone) {
  override def valid: Boolean = false

  override def offsetSecondsAt(instant: Instant): Integer = 0
}

class MomentRichTimezoneOps extends AbstractRichTimeZoneOps {
  override def parse(s: String): Xor[TimeZone.ParseError, TimeZone] = Xor.left(TimeZone.ParseError.NotImplemented)
}
