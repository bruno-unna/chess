package org.chess

import akka.actor.{ActorRef, ActorSystem}

object Chess extends App {
  val system: ActorSystem = ActorSystem("chess")

  val uci: ActorRef = system.actorOf(UCIInterpreter.props, "UCIActor")

  uci ! Start
}
