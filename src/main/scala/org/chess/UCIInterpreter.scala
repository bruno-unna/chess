package org.chess

import akka.actor.{Actor, ActorLogging, ActorRef, LoggingFSM, Props}
import org.chess.UCICommands.{Quit, UCI}

import scala.io.Source

// received events
case object Start

object UCICommands {

  sealed abstract class Command(val name: String)

  case object UCI extends Command("uci")

  case object Quit extends Command("quit")

  val commands = Seq(UCI, Quit)

  def fromString(string: String): Option[Command] = {
    val words = string.split("\\s+").toList.dropWhile(word =>
      !commands.exists(cmd => cmd.name == word.toLowerCase))
    words match {
      case first :: rest =>
        commands.find(cmd => cmd.name == first.toLowerCase)
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
      context.actorOf(LineReader.props(self)) ! Start
      goto(Ready)
  }

  when(Ready) {
    case Event(UCI, _) =>
      goto(UCIMode) using Options(ponder = true, ownBook = false)
  }

  when(UCIMode) {
    // TODO provide handlers for all possible commands
    case Event(event, data) if event != Quit =>
      stay
  }

  when(Dead) {
    case Event(event, _) =>
      log.warning("in Dead state, impossible event {} was received", event.toString)
      stay
  }

  whenUnhandled {
    case Event(Quit, _) =>
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

class LineReader(interpreter: ActorRef) extends Actor with ActorLogging {
  override def receive: PartialFunction[Any, Unit] = {
    case Start =>
      // A stream of (possibly) human interaction, the functional way:
      for (command <- Source.stdin.getLines().  // raw strings
        map(UCICommands.fromString).  // converted to commands
        filter(_.isDefined).  // but only if actual commands!
        map(_.get). // extract the command from the Option
        takeWhile(_ != Quit)  // until a Quit command arrives
      ) interpreter ! command

      // Out of the loop already? Let's quit!
      interpreter ! Quit
      context stop self
  }
}

object LineReader {
  def props(interpreter: ActorRef) = Props(new LineReader(interpreter))
}
