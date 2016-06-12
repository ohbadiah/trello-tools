package trellol

import spray.json._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import CalendarFun.parseListName
import java.time.LocalDate

final case class TrelloLabel(name: String, idBoard: String, color: String) 
final case class TrelloCard(name: String, id: String, pos: Int, labels: Seq[TrelloLabel]) 
final case class TrelloList(name: String, id: String, pos: Int) 
final case class TrelloListFull(name: String, id: String, cards: Seq[TrelloCard]) 
final case class TrelloBoard(name: String, id: String, lists: Seq[TrelloList])

trait TrelloJsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val labelFormat: RootJsonFormat[TrelloLabel] = rootFormat(lazyFormat(jsonFormat(TrelloLabel, "name", "idBoard", "color")))
  implicit val card: RootJsonFormat[TrelloCard] = rootFormat(lazyFormat(jsonFormat(TrelloCard, "name", "id", "pos", "labels")))
  implicit val fullListFormat: RootJsonFormat[TrelloListFull] = rootFormat(lazyFormat(jsonFormat(TrelloListFull, "name", "id", "cards")))
  implicit val listFormat: RootJsonFormat[TrelloList] = rootFormat(lazyFormat(jsonFormat(TrelloList, "name", "id", "pos")))
  implicit val boardFormat: RootJsonFormat[TrelloBoard] = rootFormat(lazyFormat(jsonFormat(TrelloBoard, "name", "id", "lists")))
}
