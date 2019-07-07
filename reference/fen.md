# Forsyth-Edwards Notation (FEN)

**Forsyth-Edwards Notation** (**FEN**) describes a Chess Position. It is an one-line [ASCII](http://en.wikipedia.org/wiki/ASCII)-string. **FEN** is based on a system created by Scotsman David Forsyth in the 19th century. Steven Edwards specified the **FEN standard** for computer chess applications as part of the Portable Game Notation <ref>[Standard: Portable Game Notation Specification and Implementation Guide](http://www.thechessdrum.net/PGN_Reference.txt) 16.1: FEN by Steven Edwards</ref> .

## FEN Syntax
One FEN string or record consists of **six** fields separated by a space character. The first four fields of the FEN specification are the same as the first four fields of the EPD specification.

_[Terminal and nonterminal symbols](http://en.wikipedia.org/wiki/Terminal_and_nonterminal_symbols) of a variant of [BNF](http://en.wikipedia.org/wiki/Backus%E2%80%93Naur_form) below are embedded in ' ' resp. < >._
```
<FEN> ::=  <Piece Placement>
       ' ' <Side to move>
       ' ' <Castling ability>
       ' ' <En passant target square>
       ' ' <Halfmove clock>
       ' ' <Fullmove counter>
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
If neither side can castle, the symbol '-' is used, otherwise each of four individual castling rights for king and queen castling for both sides are indicated by a sequence of one to four letters.
```
<Castling ability> ::= '-' | ['K'] ['Q'] ['k'] ['q'] (1..4)
```

### En passant target square
The en passant target square is specified after a double push of a pawn, no matter whether an en passant capture is really possible or not <ref>[Re: Arasan test suite update](http://www.talkchess.com/forum/viewtopic.php?topic_view=threads&p=219015&t=23806) by Steven Edwards, CCC, September 19, 2008</ref> <ref>[where FEN is not consistent](http://www.talkchess.com/forum/viewtopic.php?t=31521) by Reinhard Scharnagl, CCC, January 06, 2010</ref> <ref>[No more pseudolegal en passant target foolishness](http://www.talkchess.com/forum/viewtopic.php?topic_view=threads&p=396838&t=37879) by Steven Edwards, CCC, February 27, 2011</ref> . Other moves than double pawn pushes imply the symbol '-' for this FEN field.
```
<En passant target square> ::= '-' | <epsquare>
<epsquare>   ::= <fileLetter> <eprank>
<fileLetter> ::= 'a' | 'b' | 'c' | 'd' | 'e' | 'f' | 'g' | 'h'
<eprank>     ::= '3' | '6'
```
### Halfmove Clock
The halfmove clock specifies a decimal number of half moves with respect to the 50 move draw rule. It is reset to zero after a capture or a pawn move and incremented otherwise.
```
<Halfmove Clock> ::= <digit> {<digit>}
<digit> ::= '0' | '1' | '2' | '3' | '4' | '5' | '6' | '7' | '8' | '9'
```
### Fullmove counter
The number of the full moves in a game. It starts at 1, and is incremented after each Black's move.
```
<Fullmove counter> ::= <digit19> {<digit>}
<digit19> ::= '1' | '2' | '3' | '4' | '5' | '6' | '7' | '8' | '9'
<digit>   ::= '0' | <digit19>
```

## Samples
FEN strings of Starting Position and after `1.e4 c5 2.Nf3`:

[`rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1`](http://webchess.freehostia.com/diag/chessdiag.php?fen=rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR%20w%20KQkq%20-%200%201&size=small&coord=yes&cap=no&stm=yes&fb=no&theme=smart&color1=BFBCB6&color2=615F5E&color3=000000)

[`rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1`](http://webchess.freehostia.com/diag/chessdiag.php?fen=rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR%20b%20KQkq%20e3%200%201&size=small&coord=yes&cap=no&stm=yes&fb=no&theme=smart&color1=BFBCB6&color2=615F5E&color3=000000)

[`rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq c6 0 2`](http://webchess.freehostia.com/diag/chessdiag.php?fen=rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR%20w%20KQkq%20c6%200%202&size=small&coord=yes&cap=no&stm=yes&fb=no&theme=smart&color1=BFBCB6&color2=615F5E&color3=000000)

[`rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2`](http://webchess.freehostia.com/diag/chessdiag.php?fen=rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R%20b%20KQkq%20-%201%202&size=small&coord=yes&cap=no&stm=yes&fb=no&theme=smart&color1=BFBCB6&color2=615F5E&color3=000000)

## See also
* Extended Position Description (EPD)
* Forsyth-Edwards Expanded Notation (FEEN)
* Portable Game Notation (PGN)

## Forum Posts
### 2000 ...
* [File name extensions](http://www.stmintz.com/ccc/index.php?id=138460) by Leen Ammeraal, CCC, November 14, 2000 
* [Making positions in eps](http://www.stmintz.com/ccc/index.php?id=323898) by Renze Steenhuisen, CCC, October 27, 2003 » Fen2eps 
### 2005 ...
* [contradicting FEN and SMK-FEN](http://www.stmintz.com/ccc/index.php?id=439995) by Reinhard Scharnagl, CCC, August 04, 2005
* [fen to fen functions](http://www.talkchess.com/forum/viewtopic.php?t=13923) by Uri Blass, CCC, May 21, 2007
### 2010 ...
* [where FEN is not consistent](http://www.talkchess.com/forum/viewtopic.php?t=31521) by Reinhard Scharnagl, CCC, January 06, 2010
* [FEN string](http://www.talkchess.com/forum/viewtopic.php?t=37879) by colin, CCC, January 30, 2011
* [No more pseudolegal en passant target foolishness](http://www.talkchess.com/forum/viewtopic.php?topic_view=threads&p=396838&t=37879) by Steven Edwards, CCC, February 27, 2011
* [What's wrong with this EPD?](http://www.talkchess.com/forum/viewtopic.php?t=38488) by Jouni Uski, CCC, March 20, 2011
* [Question about Shredder FEN and X-FEN](http://www.talkchess.com/forum/viewtopic.php?t=43417) by Harm Geert Muller, CCC, April 22, 2012
* [Re: Causes for inconsistent benchmark signatures](http://www.talkchess.com/forum/viewtopic.php?t=47622&start=6) by Evert Glebbeek, CCC, March 27, 2013 » En passant
* [The maximum character length of a FEN string](http://www.talkchess.com/forum/viewtopic.php?t=49083) by Steven Edwards, CCC, August 24, 2013
* [Is 79 maximal?](http://www.talkchess.com/forum/viewtopic.php?t=53120) by Louis Zulli, CCC, July 29, 2014
* [PGN to FEN (with Evaluation)?](http://www.talkchess.com/forum/viewtopic.php?t=54779) by Steve Maughan, CCC, December 28, 2014 » Portable Game Notation, Python
### 2015 ...
* [Binary FEN](http://www.talkchess.com/forum/viewtopic.php?t=57065) by J. Wesley Cleveland, CCC, July 24, 2015
* [Any tool to convert FEN strings to diagrams?](http://www.talkchess.com/forum/viewtopic.php?t=59255) by Ted Wong, CCC, February 15, 2016
* [FEN - Flipper for Windows](http://www.talkchess.com/forum/viewtopic.php?t=64003) by Matthias Gemuh, CCC, May 17, 2017 » Color Flipping, [EPD

## External Links
* [Forsyth-Edwards Notation from Wikipedia](http://en.wikipedia.org/wiki/Forsyth-Edwards_Notation)
* [Chess Programming - Chess Board Implementation : A FEN parser](http://www.fam-petzke.de/cp_fen_en.shtml) by Thomas Petzke
* [FEN Database](http://mathieupage.com/?p=65) by Mathieu Pagé
* [Gilith - Chess Diagram Maker](http://www.gilith.com/chess/diagrams/) by Joe Leslie-Hurd
* [fen2img Chess Diagram Maker](http://www.gilith.com/software/fen2img/) by Joe Leslie-Hurd
* [Chess Diagram Generator](http://webchess.freehostia.com/diag/index.php)
* [Chessforeva: 3D chess diagram from FEN](http://chessforeva.blogspot.de/2009/10/3d-chess-diagram-from-fen.html) » 3D Graphics Board
* [Fen2eps](http://fen2eps.sourceforge.net/) by Dirk Baechle <ref>[Encapsulated PostScript from Wikipedia](https://en.wikipedia.org/wiki/Encapsulated_PostScript)</ref>

Original source of this document: https://www.chessprogramming.org/Forsyth-Edwards_Notation
