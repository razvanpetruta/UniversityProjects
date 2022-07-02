import random
from abc import ABC

from board.cell import Cell
from strategy.strategy import Strategy


class RandomStrategy(Strategy, ABC):
    def move(self):
        if len(self._board.get_empty_cells()) == 0:
            return None
        for i in range(4, 0, -1):
            try_win_line = self.try_win_n_in_line(i)
            if try_win_line is not None:
                return try_win_line
            try_win_column = self.try_win_n_in_columns(i)
            if try_win_column is not None:
                return try_win_column
            try_win_p_diag = self.try_win_n_on_p_diagonal(i)
            if try_win_p_diag is not None:
                return try_win_p_diag
            try_win_s_diag = self.try_win_n_on_s_diagonal(i)
            if try_win_s_diag is not None:
                return try_win_s_diag
            checked_line = self.check_n_in_line(i)
            if checked_line is not None:
                return checked_line
            checked_column = self.check_n_in_column(i)
            if checked_column is not None:
                return checked_column
            checked_p_diag = self.check_n_on_p_diagonal(i)
            if checked_p_diag is not None:
                return checked_p_diag
            checked_s_diag = self.check_n_s_diagonal(i)
            if checked_s_diag is not None:
                return checked_s_diag
        cell = random.choice(self._board.get_empty_cells())
        self._board.set_symbol(cell.line, cell.column, self._computer_symbol)
        return Cell(cell.line, cell.column, self._computer_symbol)

    def check_n_in_line(self, n):
        """
        The computer tries to block the player on row
        :param n: how many consecutive human symbols the computer is looking for
        :return: the corresponding cell or None
        """
        sol = "-"
        for i in range(n):
            sol += self._human_symbol
        sol += "-"
        sol1 = sol[1:]
        sol2 = sol[:-1]
        for line in range(self._board.lines):
            line_symbols = self._board.get_line_symbols(line)
            line_str = "".join(line_symbols)
            if sol1 in line_str:
                index = line_str.find(sol1)
                if index + n < self._board.lines:
                    self._board.set_symbol(line, index + n, self._computer_symbol)
                    return Cell(line, index + n, self._computer_symbol)
            elif sol2 in line_str:
                index = line_str.find(sol2)
                if index >= 0:
                    self._board.set_symbol(line, index, self._computer_symbol)
                    return Cell(line, index, self._computer_symbol)
        return None

    def check_n_in_column(self, n):
        """
        The computer tries to block the player on column
        :param n: how many consecutive human symbols the computer is looking for
        :return: the corresponding cell or None
        """
        sol = "-"
        for i in range(n):
            sol += self._human_symbol
        sol += "-"
        sol1 = sol[1:]
        sol2 = sol[:-1]
        for column in range(self._board.columns):
            column_symbols = self._board.get_column_symbols(column)
            column_str = "".join(column_symbols)
            if sol1 in column_str:
                index = column_str.find(sol1)
                if index + n < self._board.columns:
                    self._board.set_symbol(index + n, column, self._computer_symbol)
                    return Cell(index + n, column, self._computer_symbol)
            elif sol2 in column_str:
                index = column_str.find(sol2)
                self._board.set_symbol(index, column, self._computer_symbol)
                return Cell(index, column, self._computer_symbol)
        return None

    def check_n_on_p_diagonal(self, n):
        """
        The computer tries to block the player on diagonal from top left corner to bottom right corner
        :param n: how many consecutive human symbols the computer is looking for
        :return: the corresponding cell or None
        """
        sol = "-"
        for i in range(n):
            sol += self._human_symbol
        sol += "-"
        sol1 = sol[1:]
        sol2 = sol[:-1]
        for line in range(self._board.lines):
            p_diag_symbols = self._board.get_principal_diagonal_symbols(line, 0)
            p_diag_indexes = []
            cnt = line
            i = 0
            while cnt < self._board.lines:
                p_diag_indexes.append([cnt, i])
                i += 1
                cnt += 1
            if len(p_diag_symbols) < 5:
                continue
            p_diag_str = "".join(p_diag_symbols)
            if sol1 in p_diag_str:
                index = p_diag_str.find(sol1)
                if index + n < self._board.columns:
                    self._board.set_symbol(p_diag_indexes[index][0] + n, p_diag_indexes[index][1] + n,
                                           self._computer_symbol)
                    return Cell(p_diag_indexes[index][0] + n, p_diag_indexes[index][1] + n, self._computer_symbol)
            elif sol2 in p_diag_str:
                index = p_diag_str.find(sol2)
                self._board.set_symbol(p_diag_indexes[index][0], p_diag_indexes[index][1], self._computer_symbol)
                return Cell(p_diag_indexes[index][0], p_diag_indexes[index][1], self._computer_symbol)
            p_diag_indexes.clear()

        for column in range(1, self._board.columns):
            p_diag_symbols = self._board.get_principal_diagonal_symbols(0, column)
            p_diag_indexes = []
            cnt = column
            i = 0
            while cnt < self._board.lines:
                p_diag_indexes.append([i, cnt])
                i += 1
                cnt += 1
            if len(p_diag_symbols) < 5:
                continue
            p_diag_str = "".join(p_diag_symbols)
            if sol1 in p_diag_str:
                index = p_diag_str.find(sol1)
                if index + n < self._board.columns:
                    self._board.set_symbol(p_diag_indexes[index][0] + n, p_diag_indexes[index][1] + n,
                                           self._computer_symbol)
                    return Cell(p_diag_indexes[index][0] + n, p_diag_indexes[index][1] + n, self._computer_symbol)
            elif sol2 in p_diag_str:
                index = p_diag_str.find(sol2)
                self._board.set_symbol(p_diag_indexes[index][0], p_diag_indexes[index][1], self._computer_symbol)
                return Cell(p_diag_indexes[index][0], p_diag_indexes[index][1], self._computer_symbol)
            p_diag_indexes.clear()
        return None

    def check_n_s_diagonal(self, n):
        """
        The computer tries to block the player on diagonal from bottom left corner to top right corner
        :param n: how many consecutive human symbols the computer is looking for
        :return: the corresponding cell or None
        """
        sol = "-"
        for i in range(n):
            sol += self._human_symbol
        sol += "-"
        sol1 = sol[1:]
        sol2 = sol[:-1]
        for line in range(self._board.lines):
            s_diag_symbols = self._board.get_secondary_diagonal_symbols(line, 0)
            s_diag_indexes = []
            cnt = line
            i = 0
            while cnt >= 0:
                s_diag_indexes.append([cnt, i])
                i += 1
                cnt -= 1
            if len(s_diag_symbols) < 5:
                continue
            s_diag_str = "".join(s_diag_symbols)
            if sol1 in s_diag_str:
                index = s_diag_str.find(sol1)
                if index + n < self._board.columns:
                    self._board.set_symbol(s_diag_indexes[index][0] - n, s_diag_indexes[index][1] + n,
                                           self._computer_symbol)
                    return Cell(s_diag_indexes[index][0] - n, s_diag_indexes[index][1] + n, self._computer_symbol)
            elif sol2 in s_diag_str:
                index = s_diag_str.find(sol2)
                self._board.set_symbol(s_diag_indexes[index][0], s_diag_indexes[index][1], self._computer_symbol)
                return Cell(s_diag_indexes[index][0], s_diag_indexes[index][1], self._computer_symbol)
            s_diag_indexes.clear()

        for line in range(self._board.columns):
            s_diag_symbols = self._board.get_secondary_diagonal_symbols(line, self._board.columns - 1)
            s_diag_indexes = []
            cnt = line
            i = self._board.columns - 1
            while cnt < self._board.columns:
                s_diag_indexes.append([cnt, i])
                i -= 1
                cnt += 1
            s_diag_indexes = list(reversed(s_diag_indexes))
            if len(s_diag_symbols) < 5:
                continue
            s_diag_str = "".join(s_diag_symbols)
            if sol1 in s_diag_str:
                index = s_diag_str.find(sol1)
                if index + n < self._board.columns:
                    self._board.set_symbol(s_diag_indexes[index][0] - n, s_diag_indexes[index][1] + n,
                                           self._computer_symbol)
                    return Cell(s_diag_indexes[index][0] - n, s_diag_indexes[index][1] + n, self._computer_symbol)
            elif sol2 in s_diag_str:
                index = s_diag_str.find(sol2)
                self._board.set_symbol(s_diag_indexes[index][0], s_diag_indexes[index][1], self._computer_symbol)
                return Cell(s_diag_indexes[index][0], s_diag_indexes[index][1], self._computer_symbol)
            s_diag_indexes.clear()
        return None

    def try_win_n_in_line(self, n):
        """
        The computer is trying to win on rows.
        :param n: it checks for how many consecutive elements does he already have
        :return: the corresponding cell or None
        """
        sol = "_"
        for i in range(n):
            sol += self._computer_symbol
        sol += "-"
        sol1 = sol[1:]
        sol2 = sol[:-1]
        for line in range(self._board.lines):
            line_symbols = self._board.get_line_symbols(line)
            line_str = "".join(line_symbols)
            if sol1 in line_str:
                index = line_str.find(sol1)
                if index + n < self._board.lines:
                    self._board.set_symbol(line, index + n, self._computer_symbol)
                    return Cell(line, index + n, self._computer_symbol)
            elif sol2 in line_str:
                index = line_str.find(sol2)
                self._board.set_symbol(line, index, self._computer_symbol)
                return Cell(line, index, self._computer_symbol)

    def try_win_n_in_columns(self, n):
        """
        The computer is trying to win on columns.
        :param n: it checks for how many consecutive elements does he already have
        :return: the corresponding cell or None
        """
        sol = "-"
        for i in range(n):
            sol += self._computer_symbol
        sol += "-"
        sol1 = sol[1:]
        sol2 = sol[:-1]
        for column in range(self._board.columns):
            column_symbols = self._board.get_column_symbols(column)
            column_str = "".join(column_symbols)
            if sol1 in column_str:
                index = column_str.find(sol1)
                if index + n < self._board.columns:
                    self._board.set_symbol(index + n, column, self._computer_symbol)
                    return Cell(index + n, column, self._computer_symbol)
            elif sol2 in column_str:
                index = column_str.find(sol2)
                self._board.set_symbol(index, column, self._computer_symbol)
                return Cell(index, column, self._computer_symbol)
        return None

    def try_win_n_on_p_diagonal(self, n):
        """
        The computer tries to win on diagonal from top left corner to bottom right corner
        :param n: how many consecutive human symbols the computer is looking for
        :return: the corresponding cell or None
        """
        sol = "-"
        for i in range(n):
            sol += self._computer_symbol
        sol += "-"
        sol1 = sol.lstrip("-")
        sol2 = sol.rstrip("-")
        for line in range(self._board.lines):
            p_diag_symbols = self._board.get_principal_diagonal_symbols(line, 0)
            p_diag_indexes = []
            cnt = line
            i = 0
            while cnt < self._board.lines:
                p_diag_indexes.append([cnt, i])
                i += 1
                cnt += 1
            if len(p_diag_symbols) < 5:
                continue
            p_diag_str = "".join(p_diag_symbols)
            if sol1 in p_diag_str:
                index = p_diag_str.find(sol1)
                if index + n < self._board.columns:
                    self._board.set_symbol(p_diag_indexes[index][0] + n, p_diag_indexes[index][1] + n,
                                           self._computer_symbol)
                    return Cell(p_diag_indexes[index][0] + n, p_diag_indexes[index][1] + n, self._computer_symbol)
            elif sol2 in p_diag_str:
                index = p_diag_str.find(sol2)
                self._board.set_symbol(p_diag_indexes[index][0], p_diag_indexes[index][1], self._computer_symbol)
                return Cell(p_diag_indexes[index][0], p_diag_indexes[index][1], self._computer_symbol)
            p_diag_indexes.clear()

        for column in range(1, self._board.columns):
            p_diag_symbols = self._board.get_principal_diagonal_symbols(0, column)
            p_diag_indexes = []
            cnt = column
            i = 0
            while cnt < self._board.lines:
                p_diag_indexes.append([i, cnt])
                i += 1
                cnt += 1
            if len(p_diag_symbols) < 5:
                continue
            p_diag_str = "".join(p_diag_symbols)
            if sol1 in p_diag_str:
                index = p_diag_str.find(sol1)
                if index + 3 < self._board.columns:
                    self._board.set_symbol(p_diag_indexes[index][0] + n, p_diag_indexes[index][1] + n,
                                           self._computer_symbol)
                    return Cell(p_diag_indexes[index][0] + n, p_diag_indexes[index][1] + n, self._computer_symbol)
            elif sol2 in p_diag_str:
                index = p_diag_str.find(sol2)
                self._board.set_symbol(p_diag_indexes[index][0], p_diag_indexes[index][1], self._computer_symbol)
                return Cell(p_diag_indexes[index][0], p_diag_indexes[index][1], self._computer_symbol)
            p_diag_indexes.clear()
        return None

    def try_win_n_on_s_diagonal(self, n):
        """
        The computer tries to win on diagonal from bottom left corner to top right corner
        :param n: how many consecutive human symbols the computer is looking for
        :return: the corresponding cell or None
        """
        sol = "-"
        for i in range(n):
            sol += self._computer_symbol
        sol += "-"
        sol1 = sol.lstrip("-")
        sol2 = sol.rstrip("-")
        for line in range(self._board.lines):
            s_diag_symbols = self._board.get_secondary_diagonal_symbols(line, 0)
            s_diag_indexes = []
            cnt = line
            i = 0
            while cnt >= 0:
                s_diag_indexes.append([cnt, i])
                i += 1
                cnt -= 1
            if len(s_diag_symbols) < 5:
                continue
            s_diag_str = "".join(s_diag_symbols)
            if sol1 in s_diag_str:
                index = s_diag_str.find(sol1)
                if index + 3 < self._board.columns:
                    self._board.set_symbol(s_diag_indexes[index][0] - n, s_diag_indexes[index][1] + n,
                                           self._computer_symbol)
                    return Cell(s_diag_indexes[index][0] - n, s_diag_indexes[index][1] + n, self._computer_symbol)
            elif sol2 in s_diag_str:
                index = s_diag_str.find(sol2)
                self._board.set_symbol(s_diag_indexes[index][0], s_diag_indexes[index][1], self._computer_symbol)
                return Cell(s_diag_indexes[index][0], s_diag_indexes[index][1], self._computer_symbol)
            s_diag_indexes.clear()

        for line in range(self._board.columns):
            s_diag_symbols = self._board.get_secondary_diagonal_symbols(line, self._board.columns - 1)
            s_diag_indexes = []
            cnt = line
            i = self._board.columns - 1
            while cnt < self._board.columns:
                s_diag_indexes.append([cnt, i])
                i -= 1
                cnt += 1
            s_diag_indexes = list(reversed(s_diag_indexes))
            if len(s_diag_symbols) < 5:
                continue
            s_diag_str = "".join(s_diag_symbols)
            if sol1 in s_diag_str:
                index = s_diag_str.find(sol1)
                if index + n < self._board.columns:
                    self._board.set_symbol(s_diag_indexes[index][0] - n, s_diag_indexes[index][1] + n,
                                           self._computer_symbol)
                    return Cell(s_diag_indexes[index][0] - n, s_diag_indexes[index][1] + n, self._computer_symbol)
            elif sol2 in s_diag_str:
                index = s_diag_str.find(sol2)
                self._board.set_symbol(s_diag_indexes[index][0], s_diag_indexes[index][1], self._computer_symbol)
                return Cell(s_diag_indexes[index][0], s_diag_indexes[index][1], self._computer_symbol)
            s_diag_indexes.clear()
        return None

