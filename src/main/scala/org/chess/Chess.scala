package org.chess

import akka.actor.{ActorRef, ActorSystem}

import scala.io.Source

object Chess extends App {
  val system: ActorSystem = ActorSystem("chess")

  val uci: ActorRef = system.actorOf(UCIInterpreter.props, "UCIAutomaton")

  uci ! Start

  // A stream of (possibly) human interaction, the functional way:
  for (command <- Source.stdin.getLines(). // raw strings
    map(Command.fromString). // converted to commands
    filter(_.isDefined). // but only if actual commands!
    map(_.get). // extract the command from the Option
    takeWhile(_.keyword != Quit) // until a Quit command arrives
  ) uci ! command

  // Out of the loop already? Let's quit!
  uci ! Command(Quit, Nil)
}
