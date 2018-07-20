package org.chess

import akka.actor.{Actor, ActorLogging, ActorRef, LoggingFSM, Props}
import org.chess.UCICommands.{Nop, Quit, UCI}

import scala.io.{Source, StdIn}

// received events
case object Start

object UCICommands {

  sealed abstract class Command(val name: String)

  case object UCI extends Command("uci")

  case object Quit extends Command("quit")

  case object Nop extends Command("")

  val commands = Seq(UCI, Quit, Nop)

  def fromString(string: String): Option[Command] = commands.find(_.name == string.toLowerCase)

  def fetchCommand: UCICommands.Command = {
    lazy val input = StdIn.readLine()
    if (input == null) return Nop
    val inputStream = input.split("\\s+").toList.dropWhile(UCICommands.fromString(_).isDefined)
    if (inputStream.isEmpty) return Nop
    Nop
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
    case Event(event, data) =>
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
      println("option name Ponder type check")
      println("option name OwnBook type check default false")
      println("option name UCI_EngineAbout type string " +
        "default Chess by Bruno Unna, " +
        "see https://gitlab.com/bruno.unna/chess")
      println("uciok")
      self ! UCICommands.fetchCommand
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
      for (ln <- Source.stdin.getLines().map { line =>
        log.debug("received input \"{}\" from user", line)
        val command = UCICommands.fromString(line).getOrElse(Nop)
        log.debug("translated input to command {}", command.toString)
        interpreter ! command
        command
      }.takeWhile(_ != Quit)) { cmd: UCICommands.Command => log.debug("processed command") }
      interpreter ! Quit
      context stop self
  }
}

object LineReader {
  def props(interpreter: ActorRef) = Props(new LineReader(interpreter))
}
