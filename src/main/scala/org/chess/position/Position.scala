package org.chess.position

import akka.actor.{Actor, ActorLogging, Props}

/** Represents one particular position of the board. Note that a position and a board are different concepts:
  * the board is merely the disposition of pieces at any given moment, whereas the position includes
  * information regarding whose's turn it is, castling abilities, en-passant target square. There should be
  * no more than one instance of this actor for each position of the board.
  */
class Position(fen: String) extends Actor with ActorLogging {
  override def receive: Receive = {
    case _ =>
  }
}

object Position {
  def props(fen: String): Props = Props(new Position(fen))
}
