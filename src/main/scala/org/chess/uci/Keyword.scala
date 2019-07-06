package org.chess.uci

import enumeratum._

import scala.collection.immutable

/** Base trait of the [[org.chess.uci.Keyword]] enumeration. */
sealed trait Keyword extends EnumEntry

/** Enumeration for all possible keywords of the UCI protocol. */
object Keyword extends Enum[Keyword] {

  case object UCI extends Keyword

  case object Debug extends Keyword

  case object IsReady extends Keyword

  case object SetOption extends Keyword

  case object Register extends Keyword

  case object UCINewGame extends Keyword

  case object Position extends Keyword

  case object Go extends Keyword

  case object Stop extends Keyword

  case object PonderHit extends Keyword

  case object Quit extends Keyword

  override def values: immutable.IndexedSeq[Keyword] = findValues

}
