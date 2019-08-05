package org.chess.position

import scala.util.{Failure, Success, Try}

case class CastlingAbility(whiteKingSide: Boolean,
                           whiteQueenSide: Boolean,
                           blackKingSide: Boolean,
                           blackQueenSide: Boolean)

object CastlingAbility {

  val initial = CastlingAbility(whiteKingSide = true,
    whiteQueenSide = true,
    blackKingSide = true,
    blackQueenSide = true)

  def apply(castlingString: String): Try[CastlingAbility] = "^([KQkq]{1,4}|-)$".r.findFirstIn(castlingString) match {
    case Some(castlingString) =>
      Success(CastlingAbility(whiteKingSide = castlingString.contains("K"),
        whiteQueenSide = castlingString.contains("Q"),
        blackKingSide = castlingString.contains("k"),
        blackQueenSide = castlingString.contains("q")))
    case _ => Failure(new IllegalArgumentException("can't interpret the castling ability string"))
  }

}
