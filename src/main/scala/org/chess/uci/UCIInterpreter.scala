package org.chess.uci

import akka.actor.{LoggingFSM, Props}
import org.chess.uci.Keyword._
import org.chess.uci.State._
import org.chess.uci.UCIInterpreter._

/** Finite states machine for interpreting the UCI protocol.
  *
  * The machine starts in the [[org.chess.uci.State.Idle]] state, from which only
  * the [[org.chess.uci.UCIInterpreter.Start]] event can provoke a transition (to
  * [[org.chess.uci.State.Ready]]).
  *
  * The operational data of the machine is the [[org.chess.uci.UCIInterpreter.Options]] case
  * class, that holds all options that can be changed, usually by means
  * of the [[org.chess.uci.Keyword.SetOption]] event.
  */
class UCIInterpreter(out: String => Unit) extends LoggingFSM[State, Options] {

  // For long term operations, this signals that an ok response is pending
  var readyOkPending = false

  def info(msg: String, sendToClient: Boolean): Unit = if (sendToClient) out("info " + msg)

  def warning(msg: String, sendToClient: Boolean): Unit = {
    info("warning: " + msg, sendToClient)
    log.warning(msg)
  }

  startWith(Idle, Options(ponder = true, debug = false))

  when(Idle) {
    case Event(Start, _) =>
      goto(Ready)
  }

  when(Ready) {
    case Event(Command(UCI, _), _) =>
      goto(Waiting)
  }

  when(Waiting) {
    case Event(Command(IsReady, _), _) =>
      out("readyok")
      stay
    case Event(Command(SetOption, args), options) =>
      val pattern = "^name (.*?)(?: value (.*))?$".r
      val input = args.mkString(" ").toLowerCase
      val maybeMatch = pattern.findFirstMatchIn(input)
      val passedOption: Option[(String, Option[String])] = maybeMatch match {
        case Some(patternMatch) if patternMatch.groupCount == 2 => Some(patternMatch.group(1), Some(patternMatch.group(2)))
        case Some(patternMatch) => Some(patternMatch.group(1), None)
        case _ => None
      }
      log.debug("processing setoption, passedOption = {}", passedOption)
      val newOptions = passedOption match {
        case Some(("ponder", Some("true"))) =>
          info("ponder option is now true", options.debug)
          options.copy(ponder = true)
        case Some(("ponder", Some("false"))) =>
          info("ponder option is now false", options.debug)
          options.copy(ponder = false)
        case Some(("ponder", _)) =>
          warning("wrong parameter for command ponder, expected true or false", options.debug)
          options
        case _ => options
      }
      stay using newOptions
    case Event(Command(Register, args), _) =>
      stay
    case Event(Command(UCINewGame, _), _) =>
      // TODO provoke the reset of the system (make sure a `GameReset` command is sent at the end).
      goto(GameResetting)
    case Event(Command(Position, args), _) =>
      // TODO setup the given position on the internal board and play the given moves
      stay
    case Event(Command(Go, args), _) =>
      // TODO start thinking. When done, send a `ThinkingStopped` command.
      goto(Thinking)
  }

  when(GameResetting) {
    case Event(Command(IsReady, _), _) =>
      readyOkPending = true
      stay
    case Event(GameReset, _) =>
      goto(Waiting)
  }

  when(Thinking) {
    case Event(Command(IsReady, _), _) =>
      out("readyok")
      stay
    case Event(Command(PonderHit, _), options) =>
      // TODO the user has played the expected move. Do something. Keep searching.
      stay using options.copy(ponder = false)
    case Event(ThinkingStopped, _) =>
      // TODO use the `args` to extract result (and print it out)
      goto(Waiting)
    case Event(Command(Stop, _), _) =>
      // TODO provoke the termination of computation (make sure it sends a `ThinkingStopped` at the end).
      goto(Waiting)
  }

  when(Dead) {
    case Event(event, _) =>
      log.error("in Dead state, impossible event {} was received", event.toString)
      stay
  }

  // Use this for all messages that should be available always
  whenUnhandled {
    case Event(Command(Quit, _), _) =>
      goto(Dead)
    case Event(Command(Debug, args), options) =>
      val newOptions = if (args.exists(_.toLowerCase == "on")) options.copy(debug = true)
      else if (args.exists(_.toLowerCase == "off")) options.copy(debug = false)
      else {
        warning("invalid input to command debug (should be on or off)", options.debug)
        options
      }
      info("debug is now on", newOptions.debug)
      stay using newOptions
    case Event(Command(IsReady, _), options) =>
      log.error("command isReady is not handled in state {} but should", stateName)
      stay
    case Event(event, data) if event.isInstanceOf[Command] =>
      log.warning("while in state {}, received unhandled event {} with data {}",
        stateName, event.toString, data.toString)
      stay
  }

  onTransition {
    case Ready -> Waiting =>
      out("id name Chess 0.1.0")
      out("id author Bruno Unna")
      out(s"option name Ponder type check default ${stateData.ponder}")
      out("option name UCI_EngineAbout type string " +
        "default Chess by Bruno Unna, " +
        "see https://gitlab.com/bruno.unna/chess")
      out("uciok")
    case GameResetting -> Waiting =>
      if (readyOkPending) out("readyok")
      readyOkPending = false
    case _ -> Dead =>
      // TODO release resources, etc.
      context stop self
  }

  initialize
}

/** Provides the `Props` for the instantiation of the FSM. */
object UCIInterpreter {
  /** `Props` object to ease the creation of the FSM. */
  def props(outputFunction: String => Unit): Props = Props(new UCIInterpreter(outputFunction))

  /** Data held by the FSM when transitioning between [[org.chess.uci.State]]s. */
  case class Options(ponder: Boolean, debug: Boolean)

  /** Internal events, used to signal transitions. */
  case object Start

  case object GameReset

  case object ThinkingStopped

}
