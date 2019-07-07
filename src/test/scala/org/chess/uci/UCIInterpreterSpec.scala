package org.chess.uci

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit, TestProbe}
import org.chess.uci.Keyword._
import org.chess.uci.UCIInterpreter.Start
import org.scalatest.wordspec.AnyWordSpecLike
import org.scalatest.{BeforeAndAfterAll, Matchers}

import scala.concurrent.duration._
import scala.language.postfixOps

class UCIInterpreterSpec
  extends TestKit(ActorSystem("UCIInterpreter"))
    with ImplicitSender
    with AnyWordSpecLike
    with Matchers
    with BeforeAndAfterAll {

  private val builder: StringBuilder = new StringBuilder

  private val captureString: String => Unit = { str =>
    builder.clear
    builder.append(str)
  }

  override def afterAll: Unit = {
    super.afterAll()
    shutdown(system)
  }

  "A UCI interpreter" should {

    "get ready for action after a uci command" in {
      val interpreter = system.actorOf(UCIInterpreter.props(captureString))
      interpreter ! Start
      interpreter ! Command(UCI, Nil)
      awaitCond(builder.result() == "uciok", 3.seconds)
      interpreter ! Command(Quit, Nil)
    }

    "be able to quit" in {
      val testProbe = TestProbe()
      val interpreter = system.actorOf(UCIInterpreter.props(println))
      testProbe watch interpreter
      interpreter ! Command(Quit, Nil)
      testProbe.expectTerminated(interpreter, 3 seconds)
    }

    "inform when debug is activated" in {
      val interpreter = system.actorOf(UCIInterpreter.props(captureString))
      interpreter ! Start
      interpreter ! Command(UCI, Nil)
      interpreter ! Command(Debug, List("on"))
      awaitCond(builder.result() == "info debug is now on", 3.seconds)
    }

    "be quiet when debug is deactivated" in {
      val interpreter = system.actorOf(UCIInterpreter.props(captureString))
      interpreter ! Start
      interpreter ! Command(UCI, Nil)
      Thread.sleep(1.seconds.toMillis)
      builder.clear
      interpreter ! Command(Debug, List("off"))
      Thread.sleep(200)
      assertResult("")(builder.result)
      interpreter ! Command(Quit, Nil)
    }

    "handle 'ponder' option" in {
      val interpreter = system.actorOf(UCIInterpreter.props(captureString))
      interpreter ! Start
      interpreter ! Command(UCI, Nil)
      interpreter ! Command(Debug, List("on"))
      Thread.sleep(100)
      builder.clear
      interpreter ! Command(SetOption, List("name", "ponder", "value", "true"))
      Thread.sleep(200)
      assertResult("info ponder option is now true")(builder.result)
    }

  }

}
