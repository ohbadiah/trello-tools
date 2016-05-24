package com.nickmcavoy.trellol

import com.typesafe.config.{ConfigFactory, Config}

object TrelloL extends App {
  override def main(args: Array[String]): Unit = {
    val board: TrelloBoard = TrelloApi.getBoard()
    println("there are " + board.lists.size + " lists.")
    TrelloApi.system.shutdown()

  }

}
