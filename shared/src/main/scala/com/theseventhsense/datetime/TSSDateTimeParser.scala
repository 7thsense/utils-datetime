package com.theseventhsense.datetime

import java.util.Date

/**
 * Created by erik on 12/26/15.
 */
trait TSSDateTimeParser {
  def parse(dateTimeString: String): Date
}
