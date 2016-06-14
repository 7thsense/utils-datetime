package com.theseventhsense.datetime

import java.util.Date

object Implicits {
  implicit def dateOrdering: Ordering[Date] = Ordering.fromLessThan(_.getTime < _.getTime)
}
