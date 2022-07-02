import random

from texttable import Texttable


class Board:
    def __init__(self):
        self._size = None
        self._apples = None
        self._space = None
        self._head_i = None
        self._head_j = None
        self._current_direction = "up"
        self._current_snake = None
        self._playing = True
        self._load_data()
        self._init_board()
        self.place_snake()
        self.place_apples()

    def _load_data(self):
        """
        Load information from the file.
        """
        with open("settings.txt") as file_pointer:
            first_line = file_pointer.readline()
            first_line = first_line.rstrip("\n")
            tokens_first_line = first_line.split("=")
            self._size = int(tokens_first_line[1].strip())
            second_line = file_pointer.readline()
            second_line = second_line.rstrip("\n")
            tokens_second_line = second_line.split("=")
            self._apples = int(tokens_second_line[1].strip())

    def _init_board(self):
        """
        Initialise the empty board.
        """
        space = [[" " for i in range(self._size)] for j in range(self._size)]
        self._space = space

    def place_snake(self):
        """
        Place the snake in the middle and get the coordinates of the head.
        """
        # get the head
        head_i = self._size // 2 - 1
        head_j = self._size // 2
        self._head_i = head_i
        self._head_j = head_j
        # save the current snake
        self._current_snake = [[head_i, head_j], [head_i + 1, head_j], [head_i + 2, head_j]]
        self.draw_snake()

    def draw_snake(self):
        """
        Place the symbols for the snake.
        """
        # place the head
        self.set_symbol(self._current_snake[0][0], self._current_snake[0][1], "*")
        # place the body in the space
        for i in range(1, len(self._current_snake)):
            self.set_symbol(self._current_snake[i][0], self._current_snake[i][1], "+")

    def place_apples(self):
        """
        Place the symbols for the apples randomly.
        """
        placed_apples = 0
        while placed_apples != self._apples:
            i = random.randint(0, self._size - 1)
            j = random.randint(0, self._size - 1)
            if self.valid_apple_position(i, j):
                self.set_symbol(i, j, "a")
                placed_apples += 1

    def valid_apple_position(self, i, j):
        """
        Check if an apple can be placed on the line i, column j
        """
        if self._space[i][j] != " ":
            return False
        directions = [[-1, 0], [1, 0], [0, -1], [0, 1]]
        for direction in directions:
            i_new = i + direction[0]
            j_new = j + direction[1]
            if self.valid_index(i_new, j_new):
                if self._space[i_new][j_new] != " ":
                    return False
        return True

    def valid_index(self, i, j):
        """
        Check if matrix[i][j] is a valid position in the matrix.
        """
        return 0 <= i < self._size and 0 <= j < self._size

    def set_symbol(self, i, j, symbol):
        """
        Set a symbold to a given position.
        """
        self._space[i][j] = symbol

    def check_apple(self, i, j):
        """
        Check if we have an apple at the specified position.
        """
        return self._space[i][j] == "a"

    def check_crash_into_snake(self, i, j):
        """
        Check if the snake crashes into himself.
        """
        return [i, j] in self._current_snake

    def generate_another_apple(self):
        """
        Generate another apple when one is eaten.
        """
        placed_apples = 0
        while placed_apples != 1:
            i = random.randint(0, self._size - 1)
            j = random.randint(0, self._size - 1)
            if self.valid_apple_position(i, j):
                self.set_symbol(i, j, "a")
                placed_apples += 1

    def move_up(self):
        if self._current_direction == "up":
            return
        if self._current_direction == "down":
            raise ValueError("Cannot move 180 degrees")
        i_new = self._head_i - 1
        j_new = self._head_j
        # if it crashes into walls
        if not self.valid_index(i_new, j_new):
            self._playing = False
            return
        # if it crashes into him
        if self.check_crash_into_snake(i_new, j_new):
            self._playing = False
            return
        # if it eats an apple
        if self.check_apple(i_new, j_new):
            self.set_symbol(self._head_i, self._head_j, "+")
            self._head_i = i_new
            self._current_snake = [[i_new, j_new]] + self._current_snake
            self.set_symbol(self._head_i, self._head_j, "*")
            self.generate_another_apple()
        else:
            # just move it
            self.set_symbol(self._head_i, self._head_j, "+")
            self._head_i = i_new
            self._current_snake = [[i_new, j_new]] + self._current_snake
            self.set_symbol(self._head_i, self._head_j, "*")
            eliminated_part = self._current_snake.pop()
            self.set_symbol(eliminated_part[0], eliminated_part[1], " ")
        self._current_direction = "up"

    def move_up_no_turn(self):
        i_new = self._head_i - 1
        j_new = self._head_j
        # if it crashes into walls
        if not self.valid_index(i_new, j_new):
            self._playing = False
            return
        # if it crashes into him
        if self.check_crash_into_snake(i_new, j_new):
            self._playing = False
            return
        # if it eats an apple
        if self.check_apple(i_new, j_new):
            self.set_symbol(self._head_i, self._head_j, "+")
            self._head_i = i_new
            self._current_snake = [[i_new, j_new]] + self._current_snake
            self.set_symbol(self._head_i, self._head_j, "*")
            self.generate_another_apple()
        else:
            # just move it
            self.set_symbol(self._head_i, self._head_j, "+")
            self._head_i = i_new
            self._current_snake = [[i_new, j_new]] + self._current_snake
            self.set_symbol(self._head_i, self._head_j, "*")
            eliminated_part = self._current_snake.pop()
            self.set_symbol(eliminated_part[0], eliminated_part[1], " ")

    def move_right(self):
        if self._current_direction == "right":
            return
        if self._current_direction == "left":
            raise ValueError("Cannot move 180 degrees")
        i_new = self._head_i
        j_new = self._head_j + 1
        # if it crashes into walls
        if not self.valid_index(i_new, j_new):
            self._playing = False
            return
        # if it crashes into him
        if self.check_crash_into_snake(i_new, j_new):
            self._playing = False
            return
        # if it eats an apple
        if self.check_apple(i_new, j_new):
            self.set_symbol(self._head_i, self._head_j, "+")
            self._head_j = j_new
            self._current_snake = [[i_new, j_new]] + self._current_snake
            self.set_symbol(self._head_i, self._head_j, "*")
            self.generate_another_apple()
        else:
            # just move it
            self.set_symbol(self._head_i, self._head_j, "+")
            self._head_j = j_new
            self._current_snake = [[i_new, j_new]] + self._current_snake
            self.set_symbol(self._head_i, self._head_j, "*")
            eliminated_part = self._current_snake.pop()
            self.set_symbol(eliminated_part[0], eliminated_part[1], " ")
        self._current_direction = "right"

    def move_right_no_turn(self):
        i_new = self._head_i
        j_new = self._head_j + 1
        # if it crashes into walls
        if not self.valid_index(i_new, j_new):
            self._playing = False
            return
        # if it crashes into him
        if self.check_crash_into_snake(i_new, j_new):
            self._playing = False
            return
        # if it eats an apple
        if self.check_apple(i_new, j_new):
            self.set_symbol(self._head_i, self._head_j, "+")
            self._head_j = j_new
            self._current_snake = [[i_new, j_new]] + self._current_snake
            self.set_symbol(self._head_i, self._head_j, "*")
            self.generate_another_apple()
        else:
            # just move it
            self.set_symbol(self._head_i, self._head_j, "+")
            self._head_j = j_new
            self._current_snake = [[i_new, j_new]] + self._current_snake
            self.set_symbol(self._head_i, self._head_j, "*")
            eliminated_part = self._current_snake.pop()
            self.set_symbol(eliminated_part[0], eliminated_part[1], " ")

    def move_left(self):
        if self._current_direction == "left":
            return
        if self._current_direction == "right":
            raise ValueError("Cannot move 180 degrees")
        i_new = self._head_i
        j_new = self._head_j - 1
        # if it crashes into walls
        if not self.valid_index(i_new, j_new):
            self._playing = False
            return
        # if it crashes into him
        if self.check_crash_into_snake(i_new, j_new):
            self._playing = False
            return
        # if it eats an apple
        if self.check_apple(i_new, j_new):
            self.set_symbol(self._head_i, self._head_j, "+")
            self._head_j = j_new
            self._current_snake = [[i_new, j_new]] + self._current_snake
            self.set_symbol(self._head_i, self._head_j, "*")
            self.generate_another_apple()
        else:
            # just move it
            self.set_symbol(self._head_i, self._head_j, "+")
            self._head_j = j_new
            self._current_snake = [[i_new, j_new]] + self._current_snake
            self.set_symbol(self._head_i, self._head_j, "*")
            eliminated_part = self._current_snake.pop()
            self.set_symbol(eliminated_part[0], eliminated_part[1], " ")
        self._current_direction = "left"

    def move_left_no_turn(self):
        i_new = self._head_i
        j_new = self._head_j - 1
        # if it crashes into walls
        if not self.valid_index(i_new, j_new):
            self._playing = False
            return
        # if it crashes into him
        if self.check_crash_into_snake(i_new, j_new):
            self._playing = False
            return
        # if it eats an apple
        if self.check_apple(i_new, j_new):
            self.set_symbol(self._head_i, self._head_j, "+")
            self._head_j = j_new
            self._current_snake = [[i_new, j_new]] + self._current_snake
            self.set_symbol(self._head_i, self._head_j, "*")
            self.generate_another_apple()
        else:
            # just move it
            self.set_symbol(self._head_i, self._head_j, "+")
            self._head_j = j_new
            self._current_snake = [[i_new, j_new]] + self._current_snake
            self.set_symbol(self._head_i, self._head_j, "*")
            eliminated_part = self._current_snake.pop()
            self.set_symbol(eliminated_part[0], eliminated_part[1], " ")

    def move_down(self):
        if self._current_direction == "down":
            return
        if self._current_direction == "up":
            raise ValueError("Cannot move 180 degrees")
        i_new = self._head_i + 1
        j_new = self._head_j
        # if it crashes into walls
        if not self.valid_index(i_new, j_new):
            self._playing = False
            return
        # if it crashes into him
        if self.check_crash_into_snake(i_new, j_new):
            self._playing = False
            return
        # if it eats an apple
        if self.check_apple(i_new, j_new):
            self.set_symbol(self._head_i, self._head_j, "+")
            self._head_i = i_new
            self._current_snake = [[i_new, j_new]] + self._current_snake
            self.set_symbol(self._head_i, self._head_j, "*")
            self.generate_another_apple()
        else:
            # just move it
            self.set_symbol(self._head_i, self._head_j, "+")
            self._head_i = i_new
            self._current_snake = [[i_new, j_new]] + self._current_snake
            self.set_symbol(self._head_i, self._head_j, "*")
            eliminated_part = self._current_snake.pop()
            self.set_symbol(eliminated_part[0], eliminated_part[1], " ")
        self._current_direction = "down"

    def move_down_no_turn(self):
        i_new = self._head_i + 1
        j_new = self._head_j
        # if it crashes into walls
        if not self.valid_index(i_new, j_new):
            self._playing = False
            return
        # if it crashes into him
        if self.check_crash_into_snake(i_new, j_new):
            self._playing = False
            return
        # if it eats an apple
        if self.check_apple(i_new, j_new):
            self.set_symbol(self._head_i, self._head_j, "+")
            self._head_i = i_new
            self._current_snake = [[i_new, j_new]] + self._current_snake
            self.set_symbol(self._head_i, self._head_j, "*")
            self.generate_another_apple()
        else:
            # just move it
            self.set_symbol(self._head_i, self._head_j, "+")
            self._head_i = i_new
            self._current_snake = [[i_new, j_new]] + self._current_snake
            self.set_symbol(self._head_i, self._head_j, "*")
            eliminated_part = self._current_snake.pop()
            self.set_symbol(eliminated_part[0], eliminated_part[1], " ")

    def move(self, n):
        if self._current_direction == "up":
            for i in range(n):
                self.move_up_no_turn()
        elif self._current_direction == "down":
            for i in range(n):
                self.move_down_no_turn()
        elif self._current_direction == "right":
            for i in range(n):
                self.move_right_no_turn()
        elif self._current_direction == "left":
            for i in range(n):
                self.move_left_no_turn()

    def get_state(self):
        return self._playing

    def __str__(self):
        representation = Texttable()
        align = []
        for i in range(self._size):
            align.append("c")
        v_align = []
        for i in range(self._size):
            v_align.append("m")
        representation.set_cols_align(align)
        representation.set_cols_valign(v_align)
        for i in range(self._size):
            representation.add_row(self._space[i])
        return representation.draw()
