package org.chess.uci

import org.scalatest.wordspec.AnyWordSpecLike

class CommandSpec extends AnyWordSpecLike {

  "A command object" should {

    "silently swallow invalid inputs" in {
      val maybeCommand = Command.fromString("notAValidCommand")
      assertResult(None)(maybeCommand)
    }

    "ignore arbitrary white space" in {
      val maybeDebugCommandTrailingSpace = Command.fromString("debug on ")
      assert(maybeDebugCommandTrailingSpace.isDefined)
      val debugCommandTrailingSpace = maybeDebugCommandTrailingSpace.get
      assertResult(Keyword.Debug)(debugCommandTrailingSpace.keyword)
      assertResult(1)(debugCommandTrailingSpace.args.size)
      assertResult("on")(debugCommandTrailingSpace.args(0))

      val maybeDebugCommandWithTabs = Command.fromString("\tdebug  \t  \t\ton\t   ")
      assert(maybeDebugCommandWithTabs.isDefined)
      val debugCommandWithTabs = maybeDebugCommandWithTabs.get
      assertResult(Keyword.Debug)(debugCommandWithTabs.keyword)
      assertResult(1)(debugCommandWithTabs.args.size)
      assertResult("on")(debugCommandWithTabs.args(0))
    }

    "ignore invalid prefix in a valid command" in {
      val maybeDebugCommand = Command.fromString("joho debug on")
      assert(maybeDebugCommand.isDefined)
      val debugCommand = maybeDebugCommand.get
      assertResult(Keyword.Debug)(debugCommand.keyword)
      assertResult(1)(debugCommand.args.size)
      assertResult("on")(debugCommand.args(0))
    }

  }

}
