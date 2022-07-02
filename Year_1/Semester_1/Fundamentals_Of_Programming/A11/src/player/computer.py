from abc import ABC

from player.player import Player


class Computer(Player, ABC):
    def __init__(self, symbol, board, strategy):
        super().__init__(symbol, board)
        self.__strategy = strategy

    def move(self, line, column, symbol):
        return self.__strategy.move()
