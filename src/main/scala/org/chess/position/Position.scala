package org.chess.position

import akka.actor.{Actor, ActorLogging, Props}

import scala.util.Try

/** Represents one particular position of the board. Note that a position and a board are different concepts:
  * the board is merely the disposition of pieces at any given moment, whereas the position includes
  * information regarding whose's turn it is, castling abilities, en-passant target square. There should be
  * no more than one instance of this actor for each position of the board.
  */
class Position(fen: Fen, moves: List[String]) extends Actor with ActorLogging {
  // TODO apply the moves to the board
  override def receive: Receive = {
    case "die" => // TODO die!
    case _ =>
  }
}

object Position {
  def tryProps(args: List[String]): Try[Props] = for {
    fen <- Fen(args)
    moves = args dropWhile (_ != "moves") drop 1
  } yield Props(new Position(fen, moves))
}
