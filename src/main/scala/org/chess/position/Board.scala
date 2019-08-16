package org.chess.position

import scala.util.{Success, Try}

class Board {

  override def toString: String = ???

  def prettyPrint: String = ???

  // what's on a given square?

  // what squares are available from a given piece?

  // what captures are possible for a given piece?

  // is an en-passant capture possible, and where?

}

object Board {

  val start: Board = ??? // TODO implement the equivalent to "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR"

  def apply(piecePlacement: String): Try[Board] = Success(new Board)

}
