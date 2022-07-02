from player.computer import Computer
from player.human import Human


class Game:
    def __init__(self, board, player1, player2):
        self._board = board
        self._player1 = player1
        self._player2 = player2
        self._current_player = player1

    def play(self):
        while True:
            try:
                if self.__move(self._current_player, self._current_player.symbol):
                    return
                else:
                    if self._current_player.symbol == self._player1.symbol:
                        self._current_player = self._player2
                    else:
                        self._current_player = self._player1
            except Exception as e:
                print(str(e))
            # if self.__move(self.__player1, self.__player1.symbol):
            #     return
            # if self.__move(self.__player2, self.__player2.symbol):
            #     return

    def __move(self, player, symbol):
        line, column = -1, -1
        if type(player) is Human:
            self._draw_board()
            line, column = Game.__read_data()
        latest_move = player.move(line, column, symbol)
        if self._is_winner(latest_move) or self._is_full(latest_move):
            self._show_game_over_status(player)
            return True
        return False

    def _draw_board(self):
        print(self._board)

    def _show_game_over_status(self, player):
        print("\ngame over")
        self._draw_board()
        print(f"{player.symbol} won")

    def _is_winner(self, latest_move):
        line_symbols = self._board.get_line_symbols(latest_move.line)
        column_symbols = self._board.get_column_symbols(latest_move.column)
        p_diag_symbols = self._board.get_principal_diagonal_symbols(latest_move.line, latest_move.column)
        s_diag_symbols = self._board.get_secondary_diagonal_symbols(latest_move.line, latest_move.column)
        sol = ""
        for i in range(5):
            sol += latest_move.symbol
        s = "".join(line_symbols)
        if sol in s and sol + latest_move.symbol not in s:
            return True
        s = "".join(column_symbols)
        if sol in s and sol + latest_move.symbol not in s:
            return True
        s = "".join(p_diag_symbols)
        if sol in s and sol + latest_move.symbol not in s:
            return True
        s = "".join(s_diag_symbols)
        if sol in s and sol + latest_move.symbol not in s:
            return True
        return False

    def _is_full(self, latest_move):
        if latest_move is None:
            return True
        if len(self._board.get_empty_cells()) == 0:
            return True
        return False

    @staticmethod
    def __read_data():
        line = input("line = ")
        if not line.isnumeric():
            raise ValueError("Not a valid line")
        column = input("column = ")
        if not column.isnumeric():
            raise ValueError("Not a valid column")
        return int(line) - 1, int(column) - 1
