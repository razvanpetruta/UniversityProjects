from controller.service import Service
from src.board.board import Board
from user_interface.ui import Console

if __name__ == "__main__":
    board = Board()
    service = Service(board)
    console = Console(service)
    console.play()
