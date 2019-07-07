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

## Computational model

## References

The [Chess Programming Wiki](https://www.chessprogramming.org/Main_Page) contains a lot of useful information about many aspects of the challenge, including notational alternatives, AI, testing and tuning.

The [Portable Game Notation](http://portablegamenotation.com/index.html) site describes with detail and clarity notational aspects of the game.
