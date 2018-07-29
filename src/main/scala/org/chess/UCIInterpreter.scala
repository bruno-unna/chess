package org.chess

import akka.actor.{LoggingFSM, Props}
import enumeratum._

import scala.collection.immutable

sealed trait Keyword extends EnumEntry

object Keyword extends Enum[Keyword] {
  override def values: immutable.IndexedSeq[Keyword] = findValues

  case object UCI extends Keyword

  case object Quit extends Keyword

  case object Debug extends Keyword

  case object SetOption extends Keyword

  case object IsReady extends Keyword

}

// received events (including commands)
case object Start

case class Command(keyword: Keyword, args: List[String])

object Command {

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

// possible states
sealed trait State

case object Idle extends State

case object Ready extends State

case object UCIMode extends State

case object Dead extends State

// internal data
case class Options(ponder: Boolean, ownBook: Boolean, debug: Boolean)

import org.chess.Keyword._

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

object UCIInterpreter {
  val props: Props = Props(new UCIInterpreter)
}
