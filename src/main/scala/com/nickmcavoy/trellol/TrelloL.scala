package com.nickmcavoy.trellol

import com.typesafe.config.{ConfigFactory, Config}

object TrelloL extends App {
  override def main(args: Array[String]): Unit = {
    println("making request")
    TrelloApi.getBoard()
    TrelloApi.system.shutdown()
    
  }
  
}
