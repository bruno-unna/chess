# Standard Algebraic Notation (SAN) 
Standard algebraic notation (SAN) is the official notation of the FIDE which must be used in all recognized international competition involving human players . Concerning computer chess, SAN is a representation standard for chess moves inside the Portable Game Notation standard using the [ASCII](https://en.wikipedia.org/wiki/ASCII) [Latin alphabet](https://en.wikipedia.org/wiki/ISO/IEC_8859-1), and should be supported as default notation by all modern chess programs and their user interfaces.

While otherwise similar to LAN, SAN suppresses redundant information concerning the from-square, while keeping the descriptive letter or symbol of pieces other than a pawn. SAN further suppresses the from-to hyphen, and in some variations also the capture indicator 'x' (or ':').
```
<SAN move descriptor piece moves>   ::= <Piece symbol>[<from file>|<from rank>|<from square>]['x']<to square>
<SAN move descriptor pawn captures> ::= <from file>[<from rank>] 'x' <to square>[<promoted to>]
<SAN move descriptor pawn push>     ::= <to square>[<promoted to>]
```

## Ambiguities
If the piece is sufficient to unambiguously determine the origin square, the whole from square is omitted. Otherwise, if two (or more) pieces of the same kind can move to the same square, the piece's initial is followed by (in descending order of preference)
1. file of departure if different
1. rank of departure if the files are the same but the ranks differ
1. the complete origin square coordinate otherwise

## Captures
Captures are denoted by the lower case letter "x" immediately prior to the destination square. Pawn captures with the omitted piece symbol, include the file letter of the originating square of the capturing pawn prior to the "x" character, even if not required for unambiguousness. Some SAN variations in printed media even omit the target rank if unambiguous, like dxe, which might not be accepted as input format.

## En passant
The PGN-Standard does not require En passant captures have any special notation, and is written as if the captured pawn were on the capturing pawn's destination square.FIDE states the redundant move suffix "e.p." optional (after 1 July 2014).

`In the case of an ‘en passant’ capture, ‘e.p.’ may be appended to the notation.`

## Pawn promotion
A pawn promotion requires the information about the chosen piece, appended as trailing Piece letter behind the target square. The SAN PGN-Standard requires an equal sign ('=') immediately following the destination square.

## Castling
Castling is indicated by the special notations, "O-O" for kingside castling and "O-O-O" for queenside castling. While the FIDE handbook uses the digit zero, the SAN PGN-Standard requires the capital letter 'O' for its export format.

## Converting Moves
Due to the most compact representation, considering ambiguities concerning the origin square, converting moves with pure from- and to-squares to SAN requires not only an underlying board representation to determine piece initials, but also legal move generation for a subset of moves to the destination square. Pseudo legal, but illegal moves for instance with a Pinned piece must not be considered in ambiguous issues in an export format.

## XBoard 2
With the Chess Engine Communication Protocol version 2, one can use the feature command to select SAN as move format for both input and output .


Original source of this document: [chessprogramming.wikispaces.com](https://chessprogramming.wikispaces.com/Algebraic+Chess+Notation#Standard%20Algebraic%20Notation%20(SAN))
