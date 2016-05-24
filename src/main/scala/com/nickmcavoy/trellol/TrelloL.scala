package com.nickmcavoy.trellol

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import com.typesafe.config.{ConfigFactory, Config}

object TrelloL extends App {
  def archiveOldLists(board: TrelloBoard): Future[Seq[Boolean]] = {
    val month: String = CalendarFun.currentMonth()
    Future.sequence(
      board.lists.filter { list: TrelloList =>
        !list.name.contains(month)
      }.map{ l => TrelloApi.archiveList(l)}
    )
  }

  override def main(args: Array[String]): Unit = {
    TrelloApi.awaitIt(
      archiveOldLists(TrelloApi.getBoard())
    ).foreach {println}

    TrelloApi.system.shutdown()

  }

}
