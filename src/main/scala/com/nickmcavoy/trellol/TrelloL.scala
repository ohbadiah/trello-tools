package com.nickmcavoy.trellol

import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration.{Duration, MINUTES}
import scala.concurrent.ExecutionContext.Implicits.global

import com.typesafe.config.{ConfigFactory, Config}

object TrelloL extends App {
  def awaitIt[T](f: Future[T]): T = {
    Await.result(f, Duration(10, MINUTES))
  }

  def archiveOldLists(board: TrelloBoard): Future[Seq[Boolean]] = {
    val month: String = CalendarFun.currentMonth()
    Future.sequence(
      board.lists.filter { list: TrelloList =>
        !list.name.contains(month)
      }.map(TrelloApi.archiveList)
    )
  }

  override def main(args: Array[String]): Unit = {
    awaitIt(
      TrelloApi.getBoard().flatMap(archiveOldLists)
    ).foreach {println}

    TrelloApi.system.shutdown()

  }

}
