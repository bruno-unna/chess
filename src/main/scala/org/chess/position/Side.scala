package org.chess.position

import scala.util.Try

sealed trait Side

object Side {

  case object white extends Side

  case object black extends Side

  def apply(stringRepresentation: String): Try[Side] =
    Try(stringRepresentation.charAt(0)).map(_.toLower match {
      case 'w' => white
      case 'b' => black
    })
}
