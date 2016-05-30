package com.nickmcavoy.trellol

import java.time.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.time.LocalDate

object CalendarFun {
  val cal: Calendar = Calendar.getInstance()
  val fullMonthFormat: SimpleDateFormat = new SimpleDateFormat("MMMM")
  val yearFormat: SimpleDateFormat = new SimpleDateFormat("yyyy")
  val listFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")

  implicit def dateTimeOrdering: Ordering[LocalDate] = Ordering.fromLessThan(_ isBefore _)

  def parseListName(listName: String): LocalDate = {
    LocalDate.parse(s"${listName}, $currentYear", listFormat)
  }
  def currentMonth(): String = {
    fullMonthFormat.format(cal.getTime())
  }

  def currentYear(): String = {
    yearFormat.format(cal.getTime())
  }
}
