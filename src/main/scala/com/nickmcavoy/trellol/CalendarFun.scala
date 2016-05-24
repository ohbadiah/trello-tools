package com.nickmcavoy.trellol

import java.text.SimpleDateFormat
import java.util.Calendar

object CalendarFun {
  val cal: Calendar = Calendar.getInstance()
  val fullMonthFormat: SimpleDateFormat = new SimpleDateFormat("MMMM")
  def currentMonth(): String = {
    fullMonthFormat.format(cal.getTime())
  }

}
