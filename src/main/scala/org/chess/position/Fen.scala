package org.chess.position

import scala.util.{Failure, Success, Try}

/** Represents a single board configuration using the standard Forsyth-Edwards Notation. */
case class Fen(piecePlacement: String,
               sideToMove: Side,
               castlingAbility: CastlingAbility,
               enPassantTargetSquare: String,
               halfmoveClock: Int,
               fullmoveCounter: Int)

object Fen {
  val startpos = Fen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR", Side.white, CastlingAbility.initial, "-", 0, 1)

  def apply(fenElements: List[String]): Try[Fen] = {
    fenElements match {
      case "startpos" :: _ => Success(Fen.startpos)
      case "fen" :: piecePlacementStr :: sideToMoveStr :: castlingAbilityStr :: enPassantTargetSquareStr :: halfmoveClockStr :: fullmoveCounterStr :: _ =>
        for {
          sideToMove <- Side(sideToMoveStr)
          castlingAbility <- CastlingAbility(castlingAbilityStr)
          halfmoveClock <- Try(halfmoveClockStr.toInt)
          fullmoveCounter <- Try(fullmoveCounterStr.toInt)
          fen = Fen(piecePlacementStr, sideToMove, castlingAbility, enPassantTargetSquareStr, halfmoveClock, fullmoveCounter)
        } yield fen
      case _ => Failure(new IllegalArgumentException("can't interpret the FEN string"))
    }
  }
}
