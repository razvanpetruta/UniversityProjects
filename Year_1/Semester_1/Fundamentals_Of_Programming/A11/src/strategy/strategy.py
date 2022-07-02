from abc import ABC, abstractmethod

from board.cell import Cell


class Strategy(ABC):
    def __init__(self, board, human_symbol, computer_symbol):
        """
        General strategy constructor.
        :param board: the board on which computer plays on
        :param human_symbol: the human symbol
        :param computer_symbol: the computer
        """
        self._board = board
        self._human_symbol = human_symbol
        self._computer_symbol = computer_symbol

    @abstractmethod
    def move(self, *args) -> Cell:
        pass
