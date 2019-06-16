package org.chess

import akka.actor.ActorSystem
import akka.testkit.{TestKit, TestProbe}
import org.chess.Keyword.Quit
import org.scalatest.wordspec.AnyWordSpecLike
import org.scalatest.{BeforeAndAfterAll, Matchers}

import scala.concurrent.duration._
import scala.language.postfixOps

class UCIInterpreterSpec(_system: ActorSystem)
  extends TestKit(_system)
    with Matchers
    with AnyWordSpecLike
    with BeforeAndAfterAll {

  def this() = this(ActorSystem("UCIInterpreterSpec"))

  override def afterAll: Unit = {
    super.afterAll()
    shutdown(system)
  }

  "A UCI interpreter" should {

    "be able to quit" in {
      val testProbe = TestProbe()
      val interpreter = system.actorOf(UCIInterpreter.props)
      testProbe watch interpreter
      interpreter ! Command(Quit, Nil)
      testProbe.expectTerminated(interpreter, 3 seconds)
    }

  }

}
