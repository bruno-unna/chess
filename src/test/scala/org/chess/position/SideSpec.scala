package org.chess.position

import org.chess.position.Side.{black, white}
import org.scalatest.wordspec.AnyWordSpecLike

class SideSpec extends AnyWordSpecLike {

  "The Side object" should {
    "transform valid forms of white to the white object" in {
      val lowerW = Side("w").get
      assertResult(white)(lowerW)

      val upperW = Side("W").get
      assertResult(white)(upperW)

      val lowerWhite = Side("white").get
      assertResult(white)(lowerWhite)

      val upperWhite = Side("WHITE").get
      assertResult(white)(upperWhite)
    }

    "transform valid forms of black to the black object" in {
      val lowerB = Side("b").get
      assertResult(black)(lowerB)

      val upperB = Side("B").get
      assertResult(black)(upperB)

      val lowerBlack = Side("black").get
      assertResult(black)(lowerBlack)

      val upperBlack = Side("BLACK").get
      assertResult(black)(upperBlack)
    }

    "fail to transform a non-colour to a colour" in {
      val nonColour = Side("not a colour")
      assert(nonColour.isFailure)
    }

    "fail to transform an empty string to a colour" in {
      val empty = Side("")
      assert(empty.isFailure)
    }

  }

}
