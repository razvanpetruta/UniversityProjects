from board.cell import Cell


class BoardException(Exception):
    pass


class Board:
    def __init__(self, lines, columns, default_symbol="-"):
        """
        Board class.
        :param lines: nr. of lines
        :param columns: nr. of columns
        :param default_symbol: the default symbol
        """
        self.__lines = lines
        self.__columns = columns
        self.__default_symbol = default_symbol
        self.__cells = self.__create_board()

    @property
    def lines(self):
        return self.__lines

    @property
    def columns(self):
        return self.__columns

    def cell(self, line, column):
        return self.__cells[line][column]

    def __create_board(self):
        """
        Initialise the board
        :return: the board matrix containing the corresponding cells.
        """
        board = []
        for line in range(self.__lines):
            board_lines = []
            for column in range(self.__columns):
                board_lines.append(Cell(line, column, self.__default_symbol))
            board.append(board_lines)
        return board

    def set_symbol(self, line, column, symbol):
        """
        Set the symbol of a certain position in matrix.
        :param line: the line
        :param column: the column
        :param symbol: the symbol
        """
        if self.__cells[line][column].symbol != self.__default_symbol:
            raise BoardException(f"Position at line {line + 1} and column {column + 1} is unavailable")
        self.__cells[line][column].symbol = symbol

    def get_empty_cells(self):
        """
        :return: a list with the available cells.
        """
        empty_cells = []
        for line in self.__cells:
            for cell in line:
                if cell.symbol == "-":
                    empty_cells.append(cell)
        return empty_cells

    def get_line_symbols(self, line):
        """
        Get the symbols from a certain line.
        :param line: the line
        :return: the corresponding list
        """
        symbols = []
        for cell in self.__cells[line]:
            symbols.append(cell.symbol)
        return symbols

    def get_column_symbols(self, column):
        """
        Get the symbols from a certain column.
        :param column: the column
        :return: the corresponding list
        """
        symbols = []
        for line in self.__cells:
            symbols.append(line[column].symbol)
        return symbols

    def get_principal_diagonal_symbols(self, line, column):
        """
        Get the symbols on diagonal going from top left corner to bottom right corner.
        :param line: the line
        :param column: the column
        :return: the corresponding symbols
        """
        before = []
        after = []
        line_copy, column_copy = line - 1, column - 1
        while line_copy >= 0 and column_copy >= 0:
            before.append(self.__cells[line_copy][column_copy].symbol)
            line_copy -= 1
            column_copy -= 1
        before = list(reversed(before))
        line_copy, column_copy = line + 1, column + 1
        while line_copy < self.__lines and column_copy < self.__columns:
            after.append(self.__cells[line_copy][column_copy].symbol)
            line_copy += 1
            column_copy += 1
        return before + [self.__cells[line][column].symbol] + after

    def get_secondary_diagonal_symbols(self, line, column):
        """
        Get the list of symbols going from bottom right corner to top right corner.
        :param line: the line
        :param column: the column
        :return: the corresponding list
        """
        before = []
        after = []
        line_copy, column_copy = line - 1, column + 1
        while line_copy >= 0 and column_copy < self.__columns:
            after.append(self.__cells[line_copy][column_copy].symbol)
            line_copy -= 1
            column_copy += 1
        line_copy, column_copy = line + 1, column - 1
        while line_copy < self.__lines and column_copy >= 0:
            before.append(self.__cells[line_copy][column_copy].symbol)
            line_copy += 1
            column_copy -= 1
        before = list(reversed(before))
        return before + [self.__cells[line][column].symbol] + after

    def __str__(self):
        sol = "\n"
        for i in range(1, self.__lines + 1):
            sol += str(i)
            if i < 10:
                sol += " "
            sol += " "
        sol += "\n"
        for line in range(self.__lines):
            s_line = "  ".join(self.get_line_symbols(line))
            s_line += "  " + str(line + 1) + "\n"
            sol += s_line
        return sol

