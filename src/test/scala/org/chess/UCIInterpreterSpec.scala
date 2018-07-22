package org.chess

import akka.actor.ActorSystem
import akka.testkit.{TestKit, TestProbe}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.concurrent.duration._
import scala.language.postfixOps

class UCIInterpreterSpec(_system: ActorSystem)
  extends TestKit(_system)
    with Matchers
    with WordSpecLike
    with BeforeAndAfterAll {

  def this() = this(ActorSystem("UCIInterpreterSpec"))

  override def afterAll: Unit = {
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

    // TODO add more unit tests (but don't how how to test this, if all output goes to stdout!)
  }

}
