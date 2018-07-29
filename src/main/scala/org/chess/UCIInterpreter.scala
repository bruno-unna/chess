package org.chess

import akka.actor.{LoggingFSM, Props}
import enumeratum._

import scala.collection.immutable

/** Kick-off event, used to signal the start of operation for the FSM. */
case object Start

/** Base trait of the [[org.chess.Keyword]] enumeration. */
sealed trait Keyword extends EnumEntry

/** Enumeration for all possible keywords of the UCI protocol. */
object Keyword extends Enum[Keyword] {

  case object UCI extends Keyword

  case object Debug extends Keyword

  case object IsReady extends Keyword

  case object SetOption extends Keyword

  case object Register extends Keyword

  case object UCINewGame extends Keyword

  case object Position extends Keyword

  case object Go extends Keyword

  case object Stop extends Keyword

  case object PonderHit extends Keyword

  case object Quit extends Keyword

  override def values: immutable.IndexedSeq[Keyword] = findValues

}

/** Wrapper around the [[org.chess.Keyword]] sent from the GUI to the engine, that includes
  * the arguments of the command as a `List[String]`.
  *
  * @param keyword keyword sent by the GUI after having been understood as such
  *                (i.e. this is an entry of an enumeration, not a `String`)
  * @param args    arguments to the command as passed by the GUI
  */
case class Command(keyword: Keyword, args: List[String])

/** Contains utility methods for the [[org.chess.Command]] case class. */
object Command {

  /** Creates an optional instance of [[org.chess.Command]], given a string (as passed by the GUI).
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

sealed trait State extends EnumEntry

/** Enumeration for all possible [[org.chess.State]]s of the FSM. */
object State extends Enum[State] {

  case object Idle extends State

  case object Ready extends State

  case object UCIMode extends State

  case object Dead extends State

  override def values: immutable.IndexedSeq[State] = findValues

}

/** Data held by the FSM when transitioning between [[org.chess.State]]s. */
case class Options(ponder: Boolean, ownBook: Boolean, debug: Boolean)

import org.chess.Keyword._
import org.chess.State._

/** Finite states machine for interpreting the UCI protocol.
  *
  * The machine starts in the [[org.chess.State.Idle]] state, from which only
  * the [[org.chess.Start]] event can provoke a transition (to
  * [[org.chess.State.Ready]]).
  *
  * The operational data of the machine is the [[org.chess.Options]] case
  * class, that holds all options that can be changed, usually by means
  * of the [[org.chess.Keyword.SetOption]] event.
  */
class UCIInterpreter extends LoggingFSM[State, Options] {

  startWith(Idle, Options(ponder = true, ownBook = false, debug = false))

  when(Idle) {
    case Event(Start, _) =>
      goto(Ready)
  }

  when(Ready) {
    case Event(Command(UCI, _), _) =>
      goto(UCIMode)
  }

  when(UCIMode) {
    // TODO provide handlers for all possible commands
    case Event(Command(SetOption, _), _) =>
      stay
  }

  when(Dead) {
    case Event(event, _) =>
      log.warning("in Dead state, impossible event {} was received", event.toString)
      stay
  }

  // Use this for all messages that should be available always
  whenUnhandled {
    case Event(Command(Quit, _), _) =>
      goto(Dead)
    case Event(Command(Debug, args), options) =>
      if (args.exists(_.toLowerCase == "on")) stay using options.copy(debug = true)
      else if (args.exists(_.toLowerCase == "off")) stay using options.copy(debug = false)
      else {
        log.warning("invalid input to command debug (should be on or off)")
        stay
      }
    case Event(Command(IsReady, _), _) =>
      log.error("command isReady is not handled in state {} but should", stateName)
      stay
    case Event(event, data) if event.isInstanceOf[Command] =>
      log.warning("while in state {}, received unhandled event {} with data {}",
        stateName, event.toString, data.toString)
      stay
  }

  onTransition {
    case Ready -> UCIMode =>
      println(s"id name Chess 0.1.0")
      println(s"id author Bruno Unna")
      println("option name Ponder type check default true")
      println("option name OwnBook type check default false")
      println("option name UCI_EngineAbout type string " +
        "default Chess by Bruno Unna, " +
        "see https://gitlab.com/bruno.unna/chess")
      println("uciok")
    case _ -> Dead =>
      context stop self
      context.system.terminate()
  }

  initialize
}

/** Provides the `Props` for the instantiation of the FSM. */
object UCIInterpreter {
  val props: Props = Props(new UCIInterpreter)
}
