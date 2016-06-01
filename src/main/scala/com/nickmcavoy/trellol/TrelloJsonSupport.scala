package com.nickmcavoy.trellol

import spray.json._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import CalendarFun.parseListName
import java.time.LocalDate

final case class TrelloList(name: String, id: String, pos: Int) {
  val date: LocalDate = parseListName(name)
}
final case class TrelloBoard(name: String, id: String, lists: Seq[TrelloList])

trait TrelloJsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val listFormat: RootJsonFormat[TrelloList] = rootFormat(lazyFormat(jsonFormat(TrelloList, "name", "id", "pos")))
  implicit val boardFormat: RootJsonFormat[TrelloBoard] = rootFormat(lazyFormat(jsonFormat(TrelloBoard, "name", "id", "lists")))
}
