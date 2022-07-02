class Service:
    def __init__(self, board):
        self._board = board

    def move(self, n):
        self._board.move(n)

    def move_up(self):
        self._board.move_up()

    def move_down(self):
        self._board.move_down()

    def move_right(self):
        self._board.move_right()

    def move_left(self):
        self._board.move_left()

    def get_board_string(self):
        return str(self._board)

    def still_playing(self):
        return self._board.get_state()
