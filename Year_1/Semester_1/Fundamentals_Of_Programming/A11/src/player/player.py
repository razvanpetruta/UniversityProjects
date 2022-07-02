from abc import abstractmethod, ABC

from board.cell import Cell


class Player(ABC):
    def __init__(self, symbol, board):
        """
        General constructor for a player.
        :param symbol: the symbol of the player
        :param board: the corresponding board on which he plays on
        """
        self.__symbol = symbol
        self._board = board

    @property
    def symbol(self):
        return self.__symbol

    @abstractmethod
    def move(self, *args) -> Cell:
        pass
