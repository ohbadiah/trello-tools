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

  def addQueryParam(key: String, value: String, req: HttpRequest): HttpRequest = {
    req.withUri(
      req.uri.withQuery(
        (key -> value) +: req.uri.query()
      )
    )
  }

  def awaitIt[T](f: Future[T]): T = {
    Await.result(f, Duration(1, MINUTES))
  }

  def getBoard(): TrelloBoard = {
    val boardId: String = conf.getString("trello-api.boardId")
    val request: HttpRequest = HttpRequest(uri = s"https://api.trello.com/1/boards/$boardId")
    val responseFuture: Future[TrelloBoard] = Http().singleRequest(
      request
        .authenticate()
        .withParam("lists", "open")
        .withParam("list_fields", "name")
    ).flatMap{ resp: HttpResponse =>
      Unmarshal(resp).to[TrelloBoard]
    }
    awaitIt(responseFuture)
  }
}
