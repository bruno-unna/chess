package org.chess.position

import scala.util.{Failure, Success, Try}

/** Represents a single board configuration using the standard Forsyth-Edwards Notation. */
case class Fen(board:Board,
               sideToMove: Side,
               castlingAbility: CastlingAbility,
               enPassantTargetSquare: String,
               halfmoveClock: Int,
               fullmoveCounter: Int)

object Fen {
  val start = Fen(Board.start, Side.white, CastlingAbility.initial, "-", 0, 1)

  def apply(args: List[String]): Try[Fen] = args match {
    case "startpos" :: _ => Success(Fen.start)
    case "fen" :: piecePlacementStr :: sideToMoveStr :: castlingAbilityStr :: enPassantTargetSquareStr :: halfmoveClockStr :: fullmoveCounterStr :: _ =>
      for {
        board <- Board(piecePlacementStr)
        sideToMove <- Side(sideToMoveStr)
        castlingAbility <- CastlingAbility(castlingAbilityStr)
        halfmoveClock <- Try(halfmoveClockStr.toInt)
        fullmoveCounter <- Try(fullmoveCounterStr.toInt)
        fen = Fen(board, sideToMove, castlingAbility, enPassantTargetSquareStr, halfmoveClock, fullmoveCounter)
      } yield fen
    case _ => Failure(new IllegalArgumentException("can't interpret the FEN string"))
  }
}
