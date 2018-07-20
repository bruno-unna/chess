//#full-example
package org.chess

import akka.actor.ActorSystem
import akka.testkit.{TestKit, TestProbe}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.concurrent.duration._
import scala.language.postfixOps

//#test-classes
class ChessSpec(_system: ActorSystem)
  extends TestKit(_system)
    with Matchers
    with WordSpecLike
    with BeforeAndAfterAll {
  //#test-classes

  def this() = this(ActorSystem("ChessSpec"))

  override def afterAll: Unit = {
    shutdown(system)
  }

  //#first-test
  //#specification-example
//  "A Greeter Actor" should {
//    "pass on a greeting message when instructed to" in {
//      //#specification-example
//      val testProbe = TestProbe()
//      val helloGreetingMessage = "hello"
//      val helloGreeter = system.actorOf(Greeter.props(helloGreetingMessage, testProbe.ref))
//      val greetPerson = "Akka"
//      helloGreeter ! WhoToGreet(greetPerson)
//      helloGreeter ! Greet
//      testProbe.expectMsg(500 millis, Greeting(helloGreetingMessage + ", " + greetPerson))
//    }
//  }
  //#first-test
}

//#full-example
