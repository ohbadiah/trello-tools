package com.nickmcavoy.trellol

import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.RawHeader
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import scala.concurrent.Await
import scala.concurrent.duration.{Duration, MINUTES}
 
import com.typesafe.config.{ConfigFactory, Config}
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future
import scala.util.{Success, Failure, Try}
object TrelloApi {
  val conf: Config = ConfigFactory.load()
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  def authenticate(req: HttpRequest): HttpRequest = {
    val uri = req.uri
    req.withUri(uri.withQuery(("key" -> conf.getString("trello-api.key")) +: 
                              ("token" -> conf.getString("trello-api.token")) +:
                                uri.query()))
  }

  def awaitIt[T](f: Future[T]): T = {
    Await.result(f, Duration(1, MINUTES))
  }
  def getBoard(): Int = {
    val boardId: String = conf.getString("trello-api.boardId")
    val responseFuture: Future[Int] = Http().singleRequest(authenticate(HttpRequest(uri = s"https://api.trello.com/1/boards/$boardId"))).map{ resp: HttpResponse => 
      println(resp)
      3
    }
    awaitIt(responseFuture)
  }
}
