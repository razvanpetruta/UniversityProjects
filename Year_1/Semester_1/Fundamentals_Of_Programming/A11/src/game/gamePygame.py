import pygame

from game.game import Game
from player.computer import Computer

# Define some colors
BLACK = (0, 0, 0)
WHITE = (245, 245, 245)
RED = (133, 42, 44)
YELLOW = (208, 176, 144)

# Define grid globals
WIDTH = 20
MARGIN = 1
PADDING = 20
DOT = 4
BOARD = (WIDTH + MARGIN) * 14 + MARGIN
GAME_WIDTH = BOARD + PADDING * 2
GAME_HEIGHT = GAME_WIDTH + 100


class GamePygame(Game):
    def __init__(self, board, player1, player2):
        super().__init__(board, player1, player2)
        pygame.init()
        pygame.display.set_caption('Gomoku')
        self._display_surf = pygame.display.set_mode((GAME_WIDTH, GAME_HEIGHT), pygame.HWSURFACE | pygame.DOUBLEBUF)
        self._running = True
        self._playing = True
        self.__last_position = [-1, -1]
        self._winner = None

    def play(self):
        """
        Overwrite the play function
        """
        self.gomoku_board_init()
        if type(self._player1) is Computer:
            self._player1, self._player2 = self._player2, self._player1
            latest_move = self._player2.move(1, 1, self._player2.symbol)
            self.__last_position = [latest_move.line, latest_move.column]
            self.on_render()
            pygame.display.flip()
        while self._running:
            for event in pygame.event.get():
                self.on_event(event)
        pygame.quit()

    def on_render(self):
        """
        Render the pieces.
        """
        self.render_gomoku_piece()
        pygame.display.update()

    def render_game_info(self):
        """
        Render the winner.
        """
        color = BLACK if self._winner.symbol == self._player1.symbol else WHITE
        center = (GAME_WIDTH // 2 - 60, BOARD + 60)
        radius = 12
        pygame.draw.circle(self._display_surf, color, center, radius, 0)

        info = "You Win"
        info_font = pygame.font.SysFont('Helvetica', 24)
        text = info_font.render(info, True, BLACK)
        text_rect = text.get_rect()
        text_rect.centerx = self._display_surf.get_rect().centerx + 20
        text_rect.centery = center[1]
        self._display_surf.blit(text, text_rect)
        pygame.display.update()

    def render_gomoku_piece(self):
        """
        Render every piece.
        """
        for r in range(15):
            for c in range(15):
                center = ((MARGIN + WIDTH) * r + MARGIN + PADDING,
                          (MARGIN + WIDTH) * c + MARGIN + PADDING)
                if self._board.cell(r, c).symbol != '-':
                    color = BLACK if self._board.cell(r, c).symbol == self._player1.symbol else WHITE
                    pygame.draw.circle(self._display_surf, color,
                                       center,
                                       WIDTH // 2 - MARGIN, 0)

    def on_event(self, event):
        """
        Check the event and handle it.
        """
        if event.type == pygame.QUIT:
            self._running = False
        if event.type == pygame.MOUSEBUTTONUP and self._playing:
            pos = pygame.mouse.get_pos()
            r = (pos[0] - PADDING + WIDTH // 2) // (WIDTH + MARGIN)
            c = (pos[1] - PADDING + WIDTH // 2) // (WIDTH + MARGIN)
            if 0 <= r < 15 and 0 <= c < 15 and self._board.cell(r, c).symbol == "-":
                latest_move = self._player1.move(r, c, self._player1.symbol)
                self.__last_position = [r, c]
                self.on_render()
                if self._is_winner(latest_move):
                    self._winner = self._player1
                    self.render_game_info()
                    self._playing = False
                    return
                if self._is_full(latest_move):
                    self._playing = False
                    return
                latest_move = self._player2.move(r, c, self._player2.symbol)
                self.__last_position = [r, c]
                self.on_render()
                pygame.display.flip()
                if self._is_winner(latest_move):
                    self._winner = self._player2
                    self.render_game_info()
                    self._playing = False
                    return
                if self._is_full(latest_move):
                    self._playing = False
                    return

    def gomoku_board_init(self):
        """
        Initialise the board.
        """
        self._display_surf.fill(YELLOW)
        # Draw background rect for game area
        pygame.draw.rect(self._display_surf, BLACK,
                         [PADDING,
                          PADDING,
                          BOARD,
                          BOARD])
        # Draw the grid
        for row in range(14):
            for column in range(14):
                pygame.draw.rect(self._display_surf, YELLOW,
                                 [(MARGIN + WIDTH) * column + MARGIN + PADDING,
                                  (MARGIN + WIDTH) * row + MARGIN + PADDING,
                                  WIDTH,
                                  WIDTH])
        # Five dots
        points = [(3, 3), (11, 3), (3, 11), (11, 11), (7, 7)]
        for point in points:
            pygame.draw.rect(self._display_surf, BLACK,
                             (PADDING + point[0] * (MARGIN + WIDTH) - DOT // 2,
                              PADDING + point[1] * (MARGIN + WIDTH) - DOT // 2,
                              DOT,
                              DOT), 0)
        pygame.display.update()
