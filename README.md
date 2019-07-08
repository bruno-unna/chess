# Chess
An experiment on chess-playing, using actors as underlaying computational model.

## Overview

## Interaction with a front-end
In modern chess engines, the user interface is not catered for by the engine. Instead of that, a standard protocol is implemented, and used to interact with specialised, front-end programs, such as [Arena](http://www.playwitharena.de/) and [PyChess](http://pychess.org/).

There are several standards, of which two are outstanding:
- [Chess Engine Communication Protocol](https://www.chessprogramming.org/Chess_Engine_Communication_Protocol)
- [Universal Chess Interface](https://www.chessprogramming.org/UCI)

This program implements UCI, because it's more modern and better supported by front-end programs than CECP.

### UCI implementation
For details of the protocol, see [the UCI specification](reference/uci-engine-interface.md).

In this program the UCI is implemented as a [finite-state machine](https://en.wikipedia.org/wiki/Finite-state_machine), supported by [Akka's FSM actors](https://doc.akka.io/docs/akka/current/fsm.html).

A FSM can be described as a set of relations of the form: `State(S) x Event(E) -> Actions (A), State(S’)`. In a given state `S`, receiving an event `E` from the front-end triggers a set of actions (effects) and sets the machine to a new state `S´`.

States: `Idle` (the engine is doing nothing), `Ready` (initialised, can receive the `uci` command), `Waiting` (waiting to receive commands), `Dead` (operation is finished), `GameResetting` (reset game information), `Thinking` (calculating).

Events that can be sent by the GUI: `UCI`, `Debug`, `IsReady`, `SetOption`, `Register`, `UCINewGame`, `Position`, `Go`, `Stop`, `PonderHit`, `Quit`.

Internally generated events: `GameReset`, `ThinkingStopped`.

| State | Event | Actions | New state |
|-------|-------|---------|-----------|
| `Idle` | `Start` | Initialise | `Ready` |
| `Ready` | `UCI` | Send `id`..., send `option`..., send `uciok` | `Waiting` |
| `Waiting` | `Debug` | Set debug on/off | `Waiting` |
| `Waiting` | `IsReady` | Send `readyok` | `Waiting` |
| `Waiting` | `SetOption` | Change an option | `Waiting` |
| `Waiting` | `Register` | Send successful registration strings | `Waiting` |
| `Waiting` | `UCINewGame` | Reset game information | `GameResetting` |
| `GameResetting` | `IsReady` | Set `readyok` as pending | `GameResetting` |
| `GameResetting` | `GameReset` | Send `readyok` if pending | `Waiting` |
| `GameResetting` | `Debug` | Set debug on/off | `GameResetting` |
| `GameResetting` | `Quit` | Exit | `Dead` |
| `Waiting` | `Position` | Set internal board | `Waiting` |
| `Waiting` | `Go` | Start evaluating | `Thinking` |
| `Thinking` | `IsReady` | Send `readyok` | `Thinking` |
| `Thinking` | `PonderHit` | Deactivate pondering | `Thinking` |
| `Thinking` | `ThinkingStopped` | Stop evaluating, send `info`+`bestmove`+`ponder` | `Waiting` |
| `Thinking` | `Stop` | Stop evaluating, send `info`+`bestmove`+`ponder` | `Waiting` |
| `Thinking` | `Debug` | Set debug on/off | `Thinking` |
| `Thinking` | `Quit` | Exit | `Dead` |
| `Waiting` | `Quit` | Exit | `Dead` |
| `Dead` | - | - | `Dead` |

### Current abilities

The licensing model of this program (GPL-3.0) makes the use of the `copyprotection` response irrelevant. Also, the registration process is always instantaneously successful.

Currently, the only options supported by the program are `UCI_EngineAbout` (for which there is no equivalent `SetOption` command) and `Ponder`.

## Position representation

There are several standards for the representation of positions in chess. The two most commonly used are:
- [Forsyth-Edwards Notation](reference/fen.md) (FEN). It is very popular, and is used by UCI
- [Extended Position Description](reference/epd.md) (EPD) Richer than FEN and extensible, but not as widely adopted.

This program implements FEN, because it is required by UCI. In synthesis, a position represented in FEN consists of six fields separated by spaces:
1. Piece placement (from White's perspective). Each rank is described, starting with rank 8 and ending with rank 1; within each rank, the contents of each square are described from file "a" through file "h". Following the Standard Algebraic Notation (SAN), each piece is identified by a single letter taken from the standard English names (pawn = "P", knight = "N", bishop = "B", rook = "R", queen = "Q" and king = "K"). White pieces are designated using upper-case letters ("PNBRQK") while black pieces use lowercase ("pnbrqk"). Empty squares are noted using digits 1 through 8 (the number of empty squares), and "/" separates ranks.
1. Active color. "w" means White moves next, "b" means Black moves next.
1. Castling availability. If neither side can castle, this is "-". Otherwise, this has one or more letters: "K" (White can castle kingside), "Q" (White can castle queenside), "k" (Black can castle kingside), and/or "q" (Black can castle queenside).
1. En passant target square in algebraic notation. If there's no en passant target square, this is "-". If a pawn has just made a two-square move, this is the position "behind" the pawn. This is recorded regardless of whether there is a pawn in position to make an en passant capture.
1. Halfmove clock: This is the number of halfmoves since the last capture or pawn advance. This is used to determine if a draw can be claimed under the fifty-move rule.
1. Fullmove number: The number of the full move. It starts at 1, and is incremented after Black's move.

Here is the FEN for the starting position:

    rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1

Here is the FEN after the move 1. e4:

    rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1

And then after 1. ... c5:

    rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq c6 0 2

And then after 2. Nf3:

    rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2

## Computational model

## References

The [Chess Programming Wiki](https://www.chessprogramming.org/Main_Page) contains a lot of useful information about many aspects of the challenge, including notational alternatives, AI, testing and tuning.

The [Portable Game Notation](http://portablegamenotation.com/index.html) site describes with detail and clarity notational aspects of the game.
