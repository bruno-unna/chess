package org.chess.position

import scala.util.{Failure, Success, Try}

/** Represents a single board configuration using the standard Forsyth-Edwards Notation.
  */
case class Fen(piecePlacement: String,
               sideToMove: Side,
               castlingAbility: CastlingAbility,
               enPassantTargetSquare: String,
               halfmoveClock: String,
               fullmoveCounter: String)

object Fen {
  def apply(fenString: String): Try[Fen] = {
    // TODO validate all parameters, possibly failing
    fenString.split("\\s+").toList match {
      case piecePlacementStr :: sideToMoveStr :: castlingAbilityStr :: enPassantTargetSquareStr :: halfmoveClockStr :: fullmoveCounterStr :: Nil =>
        for {
          sideToMove <- Side(sideToMoveStr)
          castlingAbility <- CastlingAbility(castlingAbilityStr)
        } yield Fen(piecePlacementStr, sideToMove, castlingAbility, enPassantTargetSquareStr, halfmoveClockStr, fullmoveCounterStr)
      case _ => Failure(new IllegalArgumentException)
    }
  }

  def apply(): Try[Fen] = Success(Fen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR", Side.white, CastlingAbility.initial, "-", "0", "1"))
}
