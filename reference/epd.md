# Extended Position Description (EPD)

Like Forsyth-Edwards Notation, **Extended Position Description** (**EPD**) describes a chess position. Unlike FEN, **EPD** is designed to be expandable by the addition of new operations. **EPD** was developed by John Stanback and Steven Edwards. Its first implementation is in Stanback's chessplaying program Zarkov. Steven Edwards specified the **EPD** standard for computer chess applications as part of the [Portable Game Notation](http://www.thechessdrum.net/PGN_Reference.txt).

## EPD Syntax
One EPD string or record consists of one text line of variable length composed of four fields separated by a space character followed by zero or more operations. The four data fields, which describe the position, are common with the FEN-Specification.

_[Terminal and none terminal symbols](http://en.wikipedia.org/wiki/Terminal_and_nonterminal_symbols) of a variant of [BNF](http://en.wikipedia.org/wiki/Backus%E2%80%93Naur_form) below are embedded in ' ' resp. < >._
```
<EPD> ::=  <Piece Placement>
       ' ' <Side to move>
       ' ' <Castling ability>
       ' ' <En passant target square>
      {' ' <operation>}
```

### Piece Placement
The Piece Placement is determined rank by rank in big-endian order, that is starting at the 8th rank down to the first rank. Each rank is separated by the terminal symbol '/' (slash). One rank, scans piece placement in little-endian file-order from the A to H.
A decimal digit counts consecutive empty squares, the pieces are identified by a single letter from standard English names for chess pieces as used in the Algebraic chess notation. Uppercase letters are for white pieces, lowercase letters for black pieces.
```
<Piece Placement> ::= <rank8>'/'<rank7>'/'<rank6>'/'<rank5>'/'<rank4>'/'<rank3>'/'<rank2>'/'<rank1>
<ranki>       ::= [<digit17>]<piece> {[<digit17>]<piece>} [<digit17>] | '8'
<piece>       ::= <white Piece> | <black Piece>
<digit17>     ::= '1' | '2' | '3' | '4' | '5' | '6' | '7'
<white Piece> ::= 'P' | 'N' | 'B' | 'R' | 'Q' | 'K'
<black Piece> ::= 'p' | 'n' | 'b' | 'r' | 'q' | 'k'
```

### Side to move
Side to move is one lowercase letter for either White ('w') or Black ('b').
```
<Side to move> ::= {'w' | 'b'}
```

### Castling ability
If neither side can castle, the symbol '-' is used, otherwise each of four individual Castling rights for king and queen castling for both sides are indicated by a sequence of one to four letters.
```
<Castling ability> ::= '-' | ['K'] ['Q'] ['k'] ['q'] (1..4)
```

### En passant target square
The en passant target square is specified after a double push of a pawn, no matter whether an en passant capture is really possible or not. Other moves than double pawn pushes imply the symbol '-' for this FEN field.
```
<En passant target square> ::= '-' | <epsquare>
<epsquare>   ::= <fileLetter> <eprank>
<fileLetter> ::= 'a' | 'b' | 'c' | 'd' | 'e' | 'f' | 'g' | 'h'
<eprank>     ::= '3' | '6'
```

### Operations
```
<operation> ::= <opcode> {' '<operand>} ';'
<opcode>    ::= <letter> {<letter> | <digit> | '_'} (up to 14)
<operand>   ::= <stringOperand>
              | <sanMove>
              | <unsignedOperand>
              | <integerOperand>
              | <floatOperand>

<stringOperand>  ::= '"' {<char>} '"'

<sanMove>        ::= <PieceCode> [<Disambiguation>] <targetSquare> [<promotion>] ['+'|'#']
                   | <castles>
<castles>        ::= 'O-O' | 'O-O-O' (upper case O, not zero)
<PieceCode>      ::= '' | 'N' | 'B' | 'R' | 'Q' | 'K'
<Disambiguation> ::= <fileLetter> | <digit18>
<targetSquare>   ::= <fileLetter> <digit18>
<fileLetter> ::= 'a' | 'b' | 'c' | 'd' | 'e' | 'f' | 'g' | 'h'
<promotion>      ::=  '=' <PiecePromotion>
<PiecePromotion> ::= 'N' | 'B' | 'R' | 'Q'

<unsignedOperand>::= <digit19> { <digit> } | '0'
<integerOperand> ::= ['-' | '+'] <unsignedIntegerOperand>
<floatOperand>   ::= <integerOperand> '.' <digit> {<digit>}
<digit18> ::= '1' | '2' | '3' | '4' | '5' | '6' | '7' | '8'
<digit19> ::= '1' | '2' | '3' | '4' | '5' | '6' | '7' | '8' | '9'
<digit>   ::= '0' | <digit19>
```

### Opcode mnemonics
* **acd** analysis count [depth](http://www.stmintz.com/ccc/index.php?id=137052)
* **acn** analysis count Node 
* **acs** analysis count seconds
* **am** avoid move(s)
* **bm** best move(s)
* **c0** comment (primary, also **c1** though **c9**)
* **ce** Centipawns evaluation
* **dm** direct mate fullmove count
* **draw_accept** accept a draw offer
* **draw_claim** claim a draw
* **draw_offer** offer a draw
* **draw_reject** reject a draw offer
* **eco** ECO opening code
* **fmvn** fullmove number
* **hmvc** Halfmove Clock
* **id** position identification
* **nic** NIC-Key opening code
* **noop** no operation
* **pm** predicted move
* **pv** predicted variation
* **rc** repetition count
* **resign** game resignation
* **sm** supplied move
* **tcgs** telecommunication game selector
* **tcri** telecommunication receiver identification
* **tcsi** telecommunication sender identification
* **v0** variation name (primary, also **v1** though **v9**)

## See also
* Ferdinand Mosca#ChessArtist by Ferdinand Mosca
* Forsyth-Edwards Notation (FEN)
* Portable Game Notation (PGN)

## Forum Posts
* [EPD examples: Bratko-Kopec test suite](http://www.stmintz.com/ccc/index.php?id=20631) by Steven Edwards, CCC, June 15, 1998 » Bratko-Kopec Test
* [EPD format](http://www.stmintz.com/ccc/index.php?id=137052) by Stefan Meyer-Kahlen, CCC, November 07, 2000
* [Question about EPD](http://www.stmintz.com/ccc/index.php?id=155201) by Aaron Tay, CCC, February 20, 2001
* [XBoard and epd tournament](http://www.talkchess.com/forum/viewtopic.php?t=32254) by Vlad Stamate, CCC, January 31, 2010 » Chess Engine Communication Protocol, Engine Testing
* [What's wrong with this EPD?](http://www.talkchess.com/forum/viewtopic.php?t=38488) by Jouni Uski, CCC, March 20, 2011
* [epd multipv](http://www.talkchess.com/forum/viewtopic.php?t=57108) by J. Wesley Cleveland, CCC, July 28, 2015 » Principal Variation
* [Test epd for Linux ?](http://www.talkchess.com/forum/viewtopic.php?t=59633) by Jean Arthuin, CCC, March 25, 2016 » Linux, Strategic Test Suite, XBoard
* [FEN - Flipper for Windows](http://www.talkchess.com/forum/viewtopic.php?t=64003) by Matthias Gemuh, CCC, May 17, 2017 » Color Flipping, Forsyth-Edwards Notation
* [how to create a labeled epd from pgn?](http://www.talkchess.com/forum/viewtopic.php?t=65881) by Erin Dame, CCC, December 02, 2017 » Texel's Tuning Method, Portable Game Notation

## External Links
* [Standard: Portable Game Notation Specification and Implementation Guide](http://www.thechessdrum.net/PGN_Reference.txt) 16.2: EPD by Steven Edwards
* [EPD to HTML/ASCII Diagram Converter](http://www.marochess.de/php3/epd2html.html) by [Manfred Rosenboom](http://www.marochess.de/biograph.html)
* [40H Chess Tools and Utilities](http://40h.000webhostapp.com/) by Norman Pollock » Portable Game Notation
* [Chess-Tools/epd2uci.py at master · Mk-Chan/Chess-Tools · GitHub](https://github.com/Mk-Chan/Chess-Tools/blob/master/epd2uci.py) by Manik Charan » Python, python-chess 

Original source of this document: https://www.chessprogramming.org/Extended_Position_Description
