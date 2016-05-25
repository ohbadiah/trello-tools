package com.nickmcavoy.trellol

import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration.{Duration, MINUTES}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Success, Failure}

import com.typesafe.config.{ConfigFactory, Config}

object TrelloApi extends Directives with TrelloJsonSupport {
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
      .withParam(("key" -> conf.getString("trello-api.key")))
      .withParam(("token" -> conf.getString("trello-api.token")))
  }

  def awaitIt[T](f: Future[T]): T = {
    Await.result(f, Duration(10, MINUTES))
  }

  def archiveList(list: TrelloList): Future[Boolean] = {
    val request: HttpRequest = HttpRequest(uri = s"https://api.trello.com/1/lists/${list.id}/closed")
      .withMethod(HttpMethods.PUT)
      .authenticate()
      .withParam("value", "true")
    Http().singleRequest(request).map{ _ => true }.recover{ case t: Throwable => false }
  }

  def getBoard(): TrelloBoard = {
    val boardId: String = conf.getString("trello-api.boardId")
    val request: HttpRequest = HttpRequest(uri = s"https://api.trello.com/1/boards/$boardId")
      .authenticate()
      .withParam("lists", "open")
      .withParam("list_fields", "name")
    val responseFuture: Future[TrelloBoard] = Http().singleRequest(request).flatMap{ resp: HttpResponse =>
      Unmarshal(resp).to[TrelloBoard]
    }
    awaitIt(responseFuture)
  }
}
