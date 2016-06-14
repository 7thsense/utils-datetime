package com.theseventhsense.datetime

import java.util.Date
import org.widok.moment.Moment

/**
 * Created by erik on 12/26/15.
 */
object SSDateTimeParser extends TSSDateTimeParser {
  override def parse(dateTimeString: String): Date = new Date(Moment(dateTimeString).toDate().getTime().toLong)
}
