package trellol

import java.time.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.time.{LocalDate, ZoneId, DayOfWeek}
import java.time.temporal.ChronoUnit.DAYS

object CalendarFun {
  val cal: Calendar = Calendar.getInstance()
  val yearFormat: SimpleDateFormat = new SimpleDateFormat("yyyy")
  val listFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")
  val listPresentationFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d")

  implicit def dateTimeOrdering: Ordering[LocalDate] = Ordering.fromLessThan(_ isBefore _)
  implicit def dateToLocalDate(d: Date): LocalDate = {
    d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
  }

  //Days from today until Saturday, inclusive. Excludes Sunday.
  def untilSaturday(nextDay: LocalDate = cal.getTime()): Seq[LocalDate] = {
    Stream.iterate(nextDay){ nd: LocalDate => DAYS.addTo(nd, 1) }
      .takeWhile{ _.getDayOfWeek() != DayOfWeek.SUNDAY }
  }

  def parseListName(listName: String): LocalDate = {
    LocalDate.parse(s"${listName}, $currentYear", listFormat)
  }
  def daysSince(ld: LocalDate): Int = {
    DAYS.between(ld, cal.getTime()).toInt
  }

  def currentYear(): String = {
    yearFormat.format(cal.getTime())
  }
}