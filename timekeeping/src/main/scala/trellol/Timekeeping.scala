package trellol

import java.time.LocalDate

import collection.JavaConversions._
import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration.{Duration, MINUTES}
import scala.concurrent.ExecutionContext.Implicits.global
import com.typesafe.config.{ConfigFactory, Config}
import java.time.{DayOfWeek}

import CalendarFun._

object Timekeeping extends App {
  val conf: Config = ConfigFactory.load()
  val boards: Seq[BoardConf] = conf.getConfigList("trellol.timekeeping.boards").toSeq.map{ BoardConf.fromConfig }
  
  def awaitIt[T](f: Future[T]): T = {
    Await.result(f, Duration(10, MINUTES))
  }
  implicit class DaysTime(l: TrelloList) {
    def date(): LocalDate = {
      parseListName(l.name)
    }
  }

  def sortListsByDateDesc(board: TrelloBoard): Future[TrelloBoard] = {
    Future.sequence(
      board.lists
       .sortBy{ _.date}
       .reverse
       .zipWithIndex
       .map{ case (list: TrelloList, index: Int) =>
         TrelloApi.positionList(list, index+1)
       }
    ).flatMap(_ => TrelloApi.getBoard(board.id))
  }

  def ensureWeek(days: Set[DayOfWeek])(board: TrelloBoard): Future[TrelloBoard] = {
    val dateSet: Set[LocalDate] = board.lists.map{_.date}.toSet
    thisWeek().filter{ day: LocalDate =>
      !dateSet.contains(day) &&
      days.contains(day.getDayOfWeek)
    } match {
      //Nothing to do if we don't need to create any new lists.
      case Nil => Future.successful(board)
      // Create new lists and refetch board.
      case ls: Seq[LocalDate] => Future.sequence(
          ls.map{listPresentationFormat.format(_)}
            .map(TrelloApi.createList(board)_)
        ).flatMap(_ => TrelloApi.getBoard(board.id))
    }
  }

  def archiveOldLists(board: TrelloBoard): Future[TrelloBoard] = {
    Future.sequence(
      board.lists.filter { list: TrelloList =>
        daysSince(list.date) > 21
      }.map(TrelloApi.archiveList)
    ).flatMap(_ => TrelloApi.getBoard(board.id))
  }

  boards.foreach { bconf: BoardConf =>

    awaitIt(
      TrelloApi.getBoard(bconf.id)
        .flatMap(archiveOldLists)
        .flatMap(ensureWeek(bconf.days)_)
        .flatMap(sortListsByDateDesc)
    ).lists.foreach {l => println(s"${l.date}")}
  }

  TrelloApi.system.terminate()


}
