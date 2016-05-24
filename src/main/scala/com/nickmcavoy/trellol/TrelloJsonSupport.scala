package com.nickmcavoy.trellol

import spray.json._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._

final case class TrelloList(name: String, id: String)
final case class TrelloBoard(name: String, lists: Seq[TrelloList])

trait TrelloJsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val listFormat: RootJsonFormat[TrelloList] = rootFormat(lazyFormat(jsonFormat(TrelloList, "name", "id")))
  implicit val boardFormat: RootJsonFormat[TrelloBoard] = rootFormat(lazyFormat(jsonFormat(TrelloBoard, "name", "lists")))
}
