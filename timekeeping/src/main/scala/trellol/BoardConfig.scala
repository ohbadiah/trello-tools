package trellol

import com.typesafe.config.{Config}
import collection.JavaConversions._
import java.time.DayOfWeek

case class BoardConf(id: String, days: Set[DayOfWeek])

object BoardConf {

  def fromConfig(conf: Config): BoardConf = {
    BoardConf(conf.getString("id"),
      conf.getStringList("days").toSet.map{s: String => DayOfWeek.valueOf(s.toUpperCase)}
    )
  }

}
