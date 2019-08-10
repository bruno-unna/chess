package org.chess.position

import scala.util.{Success, Try}

class Board {
}

object Board {
  val start: Board = ??? // TODO implement the equivalent to "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR"

  def apply(piecePlacement: String): Try[Board] = Success(new Board)
}
