package org.chess.position

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.time.SpanSugar.convertIntToGrainOfTime
import org.scalatest.wordspec.AnyWordSpecLike
import org.scalatest.{BeforeAndAfterAll, Matchers}

import scala.language.postfixOps

class PositionSpec
  extends TestKit(ActorSystem("Board"))
    with ImplicitSender
    with AnyWordSpecLike
    with Matchers
    with BeforeAndAfterAll {

  override def afterAll: Unit = {
    super.afterAll()
    shutdown(system)
  }

  "The Position object" should {
    "accept and validate the starting position" in {
      val triedProps = Position.tryProps("startpos" :: Nil)
      assert(triedProps.isSuccess)
      val p = system actorOf triedProps.get
      p ! Position.getBoard
      val board = expectMsgType[Board](100 milliseconds)
    }

    "reject an invalid position" in {}

    "correctly apply valid moves" in {}

    "reject an invalid move" in {}
  }
}
