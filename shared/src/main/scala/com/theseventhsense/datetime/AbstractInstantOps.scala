package com.theseventhsense.datetime

import com.theseventhsense.utils.types.SSDateTime

/**
 * Created by erik on 3/19/16.
 */
abstract class AbstractInstantOps {
  def instantToLongString(instant: SSDateTime.Instant, timeZone: SSDateTime.TimeZone): String
}
