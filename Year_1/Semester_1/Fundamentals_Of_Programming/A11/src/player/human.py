from abc import ABC

from board.cell import Cell
from player.player import Player


class Human(Player, ABC):
    def move(self, line, column, symbol):
        self._board.set_symbol(line, column, symbol)
        return Cell(line, column, symbol)
