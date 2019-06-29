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
In this program UCI is implemented as a [finite-state machine](https://en.wikipedia.org/wiki/Finite-state_machine), supported by [Akka's FSM actors](https://doc.akka.io/docs/akka/current/fsm.html). 

## Computational model

## References

The [Chess Programming Wiki](https://www.chessprogramming.org/Main_Page) contains a lot of useful information about many aspects of the challenge, including notational alternatives, AI, testing and tuning.

The [Portable Game Notation](http://portablegamenotation.com/index.html) site describes with detail and clarity notational aspects of the game.
