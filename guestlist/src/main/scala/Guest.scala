package trellol

sealed trait Guest {
  def numberOfPeople: Int
}
case class Friend(firstName: String, lastName: Option[String]) extends Guest {
  val numberOfPeople = 1
}
case class Couple(familyName: Option[String], fname1: String, fname2: Option[String]) extends Guest {
  val numberOfPeople = 2
}
case class Family(familyName: String, husbandName: String, wifeName: String, childNames: List[String]) extends Guest {
  val numberOfPeople = 2 + childNames.length
}


object Guests {
  val nameCharacter = "[A-Za-z'\\-]"
  val w = s"($nameCharacter+)"
  val specialFriend = s"$w".r
  val friendRegex = s"$w $w".r
  val specialCouple = s"$w and $w".r
  val coupleA     = s"$w and $w $w".r
  val coupleA2    = s"$w \\+ $w $w".r
  val coupleB     = s"$w $w and $w".r
  val coupleB2    = s"$w $w \\+ $w".r
  val coupleUnknownPartner = s"$w $w \\+".r
  val coupleTwoNames = s"$w $w and $w $nameCharacter+".r
  def parse(name: String): Either[String, Guest] = name match {
    case specialFriend(first) => Right(Friend(first, None))
    case friendRegex(first, last) => Right(Friend(first, Some(last)))
    case specialCouple(fname1, fname2) => Right(Couple(None, fname1, Some(fname2)))
    case coupleA(fname1, fname2, familyName) => Right(Couple(Some(familyName), fname1, Some(fname2)))
    case coupleB(fname1, familyName, fname2) => Right(Couple(Some(familyName), fname1, Some(fname2)))
    case coupleB2(fname1, familyName, fname2) => Right(Couple(Some(familyName), fname1, Some(fname2)))
    case coupleTwoNames(fname1, familyName, fname2) => Right(Couple(Some(familyName), fname1, Some(fname2)))
    case coupleUnknownPartner(fname1, familyName) => Right(Couple(Some(familyName), fname1, None))
    case _ => Left(s"Unknown: $name")
  }

}
