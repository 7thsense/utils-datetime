package com.theseventhsense.datetime

import cats.data.Xor
import com.theseventhsense.utils.types.SSDateTime.DateTime

/**
 * Created by erik on 12/26/15.
 */
trait TSSDateTimeParser {
  def parse(dateTimeString: String): Xor[DateTime.ParseError, DateTime]
}
