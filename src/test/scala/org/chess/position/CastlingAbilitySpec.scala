package org.chess.position

import org.scalatest.wordspec.AnyWordSpecLike

class CastlingAbilitySpec extends AnyWordSpecLike {

  "The CastlingAbility object" should {
    "understand full castling options" in {
      val full = CastlingAbility("QKkq").get
      assert(full.whiteKingSide)
      assert(full.whiteQueenSide)
      assert(full.blackKingSide)
      assert(full.blackQueenSide)
    }

    "understand partial castling options" in {
      val partial = CastlingAbility("qK").get
      assert(partial.whiteKingSide)
      assert(!partial.whiteQueenSide)
      assert(!partial.blackKingSide)
      assert(partial.blackQueenSide)
    }

    "understand the lack of castling options" in {
      val noCastling = CastlingAbility("-").get
      assert(!noCastling.whiteKingSide)
      assert(!noCastling.whiteQueenSide)
      assert(!noCastling.blackKingSide)
      assert(!noCastling.blackQueenSide)
    }

    "fail with wrong characters" in {
      val wrong = CastlingAbility("uy")
      assert(wrong.isFailure)
    }

  }

}
