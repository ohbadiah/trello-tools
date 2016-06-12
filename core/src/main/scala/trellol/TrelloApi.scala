package trellol

import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Success, Failure}

import com.typesafe.config.{ConfigFactory, Config}

object TrelloApi extends TrelloJsonSupport {
  val conf: Config = ConfigFactory.load()
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  implicit class EasyQueryParams(req: HttpRequest) {
    def withParam(tup: (String, String)): HttpRequest = req.withUri(
        req.uri.withQuery(
          tup +: req.uri.query()
        )
      )
    def authenticate(): HttpRequest = req
      .withParam(("key" -> conf.getString("trellol.trello-api.key")))
      .withParam(("token" -> conf.getString("trellol.trello-api.token")))
  }

  def archiveList(list: TrelloList): Future[TrelloList] = {
    val request: HttpRequest = HttpRequest(uri = s"https://api.trello.com/1/lists/${list.id}/closed")
      .withMethod(HttpMethods.PUT)
      .authenticate()
      .withParam("value", "true")
      Http().singleRequest(request).map{ _ => list }
  }

  def positionList(list: TrelloList, pos: Int): Future[TrelloList] = {
    val request: HttpRequest = HttpRequest(uri = s"https://api.trello.com/1/lists/${list.id}/pos")
      .withMethod(HttpMethods.PUT)
      .authenticate()
      .withParam("value", s"$pos")
    Http().singleRequest(request).flatMap{Unmarshal(_).to[TrelloList]}
  }

  def getList(listId: String): Future[TrelloListFull] = {
    val request: HttpRequest = HttpRequest(uri = s"https://api.trello.com/1/lists/${listId}")
      .authenticate()
      .withParam("cards", "open")
      .withParam("card_fields", "name,id,pos,labels")
    Http().singleRequest(request).flatMap{Unmarshal(_).to[TrelloListFull]}
  }

  def createList(board: TrelloBoard)(listName: String): Future[TrelloList] = {
    val request: HttpRequest = HttpRequest(uri = s"https://api.trello.com/1/lists")
      .withMethod(HttpMethods.POST)
      .authenticate()
      .withParam("idBoard", board.id)
      .withParam("name", listName)
      Http().singleRequest(request).flatMap{Unmarshal(_).to[TrelloList]}
  }

  def getBoard(boardId: String): Future[TrelloBoard] = {
    val request: HttpRequest = HttpRequest(uri = s"https://api.trello.com/1/boards/$boardId")
      .authenticate()
      .withParam("lists", "open")
      .withParam("list_fields", "name,pos")
    Http().singleRequest(request).flatMap{ resp: HttpResponse =>
      Unmarshal(resp).to[TrelloBoard]
    }
  }
}
