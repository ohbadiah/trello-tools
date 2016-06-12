package trellol

import java.time.LocalDate

import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration.{Duration, MINUTES}
import scala.concurrent.ExecutionContext.Implicits.global

import com.typesafe.config.{ConfigFactory, Config}

import CalendarFun._

object Timekeeping extends App {
  def awaitIt[T](f: Future[T]): T = {
    Await.result(f, Duration(10, MINUTES))
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
    ).flatMap(_ => TrelloApi.getBoard)
  }

  def ensureUntilSaturday(board: TrelloBoard): Future[TrelloBoard] = {
    val dateSet: Set[LocalDate] = board.lists.map{_.date}.toSet
    untilSaturday().filter{
      !dateSet.contains(_)
    } match {
      //Nothing to do if we don't need to create any new lists.
      case Nil => Future.successful(board)
      // Create new lists and refetch board.
      case ls: Seq[LocalDate] => Future.sequence(
          ls.map{listPresentationFormat.format(_)}
            .map(TrelloApi.createList(board)_)
        ).flatMap(_ => TrelloApi.getBoard)
    }
  }

  def archiveOldLists(board: TrelloBoard): Future[TrelloBoard] = {
    Future.sequence(
      board.lists.filter { list: TrelloList =>
        daysSince(list.date) > 21
      }.map(TrelloApi.archiveList)
    ).flatMap(_ => TrelloApi.getBoard)
  }

  awaitIt(
    TrelloApi.getBoard()
      .flatMap(archiveOldLists)
      .flatMap(ensureUntilSaturday)
      .flatMap(sortListsByDateDesc)
  ).lists.foreach {l => println(s"${l.date}")}

  TrelloApi.system.terminate()


}