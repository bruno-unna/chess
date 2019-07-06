package org.chess.uci

/** Wrapper around the [[org.chess.uci.Keyword]] sent from the GUI to the engine, that includes
  * the arguments of the command as a `List[String]`.
  *
  * @param keyword keyword sent by the GUI after having been understood as such
  *                (i.e. this is an entry of an enumeration, not a `String`)
  * @param args    arguments to the command as passed by the GUI
  */
case class Command(keyword: Keyword, args: List[String])

/** Contains utility methods for the [[org.chess.uci.Command]] case class. */
object Command {

  /** Creates an optional instance of [[org.chess.uci.Command]], given a string (as passed by the GUI).
    *
    * @param string what the GUI sends
    * @return `Some[Command]` if the string can be parsed as such, `None` otherwise
    */
  def fromString(string: String): Option[Command] = {
    val words = string.split("\\s+").toList.
      dropWhile(Keyword.withNameInsensitiveOption(_).isEmpty)
    words match {
      case keyword :: arguments =>
        Keyword.withNameInsensitiveOption(keyword).map(Command(_, arguments))
      case _ =>
        None
    }
  }
}
