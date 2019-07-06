package org.chess

import akka.actor.{ActorRef, ActorSystem}
import org.chess.uci.Keyword.Quit
import org.chess.uci.UCIInterpreter.Start
import org.chess.uci.{Command, UCIInterpreter}

import scala.io.Source

/** Entry point of the application.
  *
  * Responsibilities of this object are:
  * - Initialising the actor system for the UCI protocol FSM.
  * - Reading from the `stdin` (as per the protocol) and sending valid commands to the FSM.
  * - Signal the FSM to quit when the moment to do so comes (quit command).
  */
object Chess extends App {
  val system: ActorSystem = ActorSystem("chess")

  val uci: ActorRef = system.actorOf(UCIInterpreter.props(println), "UCIAutomaton")

  uci ! Start

  // A stream of (possibly) human interaction, the functional way:
  for (command <- Source.stdin.getLines() // raw strings
    .map(Command.fromString) // converted to commands
    .filter(_.isDefined) // but only if actual commands!
    .map(_.get) // extract the command from the Option
    .takeWhile(_.keyword != Quit) // until a Quit command arrives
  ) uci ! command

  // Out of the loop already? Let's quit!
  uci ! Command(Quit, Nil)
  system.terminate()
}
