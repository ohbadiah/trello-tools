package trellol

import org.scalatest._

class GuestSpec extends FlatSpec with Matchers {
  val sampleNames: Seq[String] = List(
    "Karen",
    "Jay Jennings",
    "Don and Jenny Mangialardi",
    "Josh McAvoy and Samantha",
    "Lucy Myers +"


  )

  "The sample names" should "all parse with a Right" in {
    sampleNames.map(Guests.parse).foreach { println }
    sampleNames.map(Guests.parse).foreach { _ should be ('right)}
  }



}
