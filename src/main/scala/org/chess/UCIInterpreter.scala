package org.chess

import akka.actor.{LoggingFSM, Props}

// received events (including commands)
case object Start

sealed abstract class Keyword(val name: String)

case object UCI extends Keyword("uci")

case object Quit extends Keyword("quit")

case object Debug extends Keyword("debug")

case class Command(keyword: Keyword, args: List[String])

object Command {

  val keywords: Seq[Keyword] = Seq(UCI, Quit, Debug)

  def fromString(string: String): Option[Command] = {
    val words = string.split("\\s+").toList.
      dropWhile(word => !keywords.exists(_.name == word.toLowerCase))
    words match {
      case keyword :: arguments =>
        keywords.find(_.name == keyword.toLowerCase).map(Command(_, arguments))
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
sealed trait Data

case object Uninitialised extends Data

final case class Options(ponder: Boolean, ownBook: Boolean) extends Data

class UCIInterpreter extends LoggingFSM[State, Data] {

  startWith(Idle, Uninitialised)

  when(Idle) {
    case Event(Start, _) =>
      goto(Ready)
  }

  when(Ready) {
    case Event(Command(UCI, _), _) =>
      goto(UCIMode) using Options(ponder = true, ownBook = false)
  }

  when(UCIMode) {
    // TODO provide handlers for all possible commands
    case Event(Command(keyword, _), _) if keyword != Quit =>
      stay
  }

  when(Dead) {
    case Event(event, _) =>
      log.warning("in Dead state, impossible event {} was received", event.toString)
      stay
  }

  whenUnhandled {
    case Event(Command(Quit, _), _) =>
      goto(Dead)
    case Event(event, data) =>
      log.warning("in state {}, received unhandled event {} with data {}", stateName, event.toString, data.toString)
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
