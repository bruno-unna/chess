package org.chess.uci

import enumeratum._

import scala.collection.immutable

sealed trait State extends EnumEntry

/** Enumeration for all possible [[org.chess.uci.State]]s of the FSM. */
object State extends Enum[State] {

  case object Idle extends State

  case object Ready extends State

  case object Waiting extends State

  case object Dead extends State

  case object GameResetting extends State

  case object Thinking extends State

  override def values: immutable.IndexedSeq[State] = findValues

}
