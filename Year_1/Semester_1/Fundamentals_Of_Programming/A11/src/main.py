from board.board import Board
from game.game import Game
from game.gamePygame import GamePygame
from player.computer import Computer
from player.human import Human
from strategy.randomStrategy import RandomStrategy

# this is the main function
if __name__ == "__main__":
    board = Board(15, 15)
    strategy = RandomStrategy(board, "X", "0")
    player1 = Human("X", board)
    player2 = Computer("0", board, strategy)
    want_ui = input("\ndo you want GUI? y/n\n")
    game = GamePygame(board, player1, player2) if want_ui == "y" else Game(board, player1, player2)
    game.play()
