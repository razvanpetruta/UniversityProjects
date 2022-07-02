import unittest

from board.board import Board, BoardException
from board.cell import Cell
from player.computer import Computer
from player.human import Human
from strategy.randomStrategy import RandomStrategy


class BoardTest(unittest.TestCase):
    def setUp(self) -> None:
        self.__board = Board(5, 5)

    def test_string_representation(self):
        self.assertEqual(str(self.__board), "\n1  2  3  4  5  \n"
                                            "-  -  -  -  -  1\n"
                                            "-  -  -  -  -  2\n"
                                            "-  -  -  -  -  3\n"
                                            "-  -  -  -  -  4\n"
                                            "-  -  -  -  -  5\n")

    def test_correct_lengths(self):
        self.assertEqual(self.__board.lines, 5)
        self.assertEqual(self.__board.columns, 5)

    def test_set_symbol(self):
        self.__board.set_symbol(1, 1, "a")
        self.assertEqual(self.__board.cell(1, 1).symbol, "a")
        with self.assertRaises(BoardException) as be:
            self.__board.set_symbol(1, 1, "c")

    def test_empty_cells(self):
        self.assertEqual(len(self.__board.get_empty_cells()), 25)
        self.__board.set_symbol(1, 1, "a")
        self.assertEqual(len(self.__board.get_empty_cells()), 24)

    def test_get_line_symbols(self):
        self.__board.set_symbol(1, 1, "a")
        self.assertEqual(self.__board.get_line_symbols(1), ["-", "a", "-", "-", "-"])

    def test_get_column_symbols(self):
        self.__board.set_symbol(1, 2, "a")
        self.assertEqual(self.__board.get_column_symbols(2), ["-", "a", "-", "-", "-"])

    def test_get_principal_diagonal_symbols(self):
        self.__board.set_symbol(3, 3, "1")
        self.assertEqual(self.__board.get_principal_diagonal_symbols(3, 3), ["-", "-", "-", "1", "-"])

    def test_get_secondary_diagonal_symbols(self):
        self.__board.set_symbol(3, 3, "1")
        self.assertEqual(self.__board.get_secondary_diagonal_symbols(3, 3), ["-", "1", "-"])


class CellTest(unittest.TestCase):
    def test_cell(self):
        c = Cell(1, 1, "1")
        self.assertEqual(c.line, 1)
        self.assertEqual(c.column, 1)
        self.assertEqual(c.symbol, "1")
        c.line = 2
        c.column = 2
        c.symbol = "2"
        self.assertEqual(c.line, 2)
        self.assertEqual(c.column, 2)
        self.assertEqual(c.symbol, "2")


class StrategyTest(unittest.TestCase):
    def test_strategy(self):
        board = Board(6, 6)
        strategy = RandomStrategy(board, "X", "0")
        player1 = Human("X", board)
        player2 = Computer("0", board, strategy)
        player1.move(1, 1, "X")
        player2.move(3, 3, 3)
        self.assertEqual(board.cell(1, 2).symbol, "0")
        player1.move(2, 1, "X")
        player2.move(3, 3, 3)
        self.assertEqual(board.cell(3, 1).symbol, "0")
        player1.move(2, 2, "X")
        player2.move(3, 3, 3)
        self.assertEqual(board.cell(2, 3).symbol, "0")
        player1.move(3, 3, "X")
        player2.move(3, 3, 3)
        self.assertEqual(board.cell(4, 4).symbol, "0")
        player1.move(5, 0, "X")
        player2.move(3, 3, 3)
        self.assertEqual(board.cell(0, 0).symbol, "0")
        player1.move(0, 5, "X")
        player2.move(3, 3, 3)
        self.assertEqual(board.cell(3, 4).symbol, "0")
        player1.move(0, 1, "X")
        player2.move(3, 3, 3)
        self.assertEqual(board.cell(4, 5).symbol, "0")
        player1.move(5, 1, "X")
        player2.move(3, 3, 3)
        self.assertEqual(board.cell(5, 4).symbol, "0")
        player1.move(5, 2, "X")
        player2.move(3, 3, 3)
        self.assertEqual(board.cell(2, 4).symbol, "0")
        player1.move(5, 3, "X")
        player2.move(3, 3, 3)
        self.assertEqual(board.cell(1, 4).symbol, "0")

