class Console:
    def __init__(self, service):
        self._service = service

    @staticmethod
    def read_command():
        command = input("command: ")
        tokens = command.split()
        if tokens[0] not in ["move", "up", "down", "right", "left"]:
            raise ValueError("Invalid command")
        if len(tokens) > 1:
            tokens[1] = tokens[1].strip()
            if not tokens[1].isnumeric():
                raise ValueError("Not correct numbers of moves")
            return tokens[0], int(tokens[1])
        return command, 1

    def print_board(self):
        print(self._service.get_board_string())
        print("")

    def play(self):
        while True:
            try:
                self.print_board()
                command, n = Console.read_command()
                if command == "up":
                    self._service.move_up()
                if command == "down":
                    self._service.move_down()
                if command == "left":
                    self._service.move_left()
                if command == "right":
                    self._service.move_right()
                if command == "move":
                    self._service.move(n)
                if not self._service.still_playing():
                    print("game over")
                    return
            except Exception as e:
                print(str(e))


