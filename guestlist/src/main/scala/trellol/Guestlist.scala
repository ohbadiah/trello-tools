package trellol

import com.typesafe.config.{ConfigFactory, Config}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


object Guestlist extends App {
  val conf: Config = ConfigFactory.load()
  val boardId: String = conf.getString("trellol.guestlist.boardId")

  def allFullLists(board: TrelloBoard): Future[Seq[TrelloListFull]] = {
    Future.sequence(
      board.lists.map { case TrelloList(_, id, _) => {
          TrelloApi.getList(id) 
        }
      }
    )
  }

  val fullBoard: Future[Seq[TrelloListFull]] = TrelloApi.getBoard(boardId)
    .flatMap(allFullLists)

  fullBoard.onSuccess{ case lists: Seq[TrelloListFull] => 
    val guestlist = lists.flatMap(_.cards).map{c: TrelloCard => Guests.parse(c.name)}
    val guestTotal: Int = guestlist.filter{_.isRight}.map{_.right.get.numberOfPeople}.reduceLeft{_ + _}
    println("Couldn't parse: ")
    guestlist.filter{_.isLeft}.foreach(println)
    println(s"Total of $guestTotal guests")
  }
  fullBoard.onComplete { case _ => 
    TrelloApi.system.terminate() 
  }
}
