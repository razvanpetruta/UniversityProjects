class Cell:
    def __init__(self, line, column, symbol):
        """
        Constructor for Cell type object.
        :param line: the line
        :param column: the column
        :param symbol: the diagonal
        """
        self.__line = line
        self.__column = column
        self.__symbol = symbol

    @property
    def line(self):
        return self.__line

    @line.setter
    def line(self, value):
        self.__line = value

    @property
    def column(self):
        return self.__column

    @column.setter
    def column(self, value):
        self.__column = value

    @property
    def symbol(self):
        return self.__symbol

    @symbol.setter
    def symbol(self, value):
        self.__symbol = value
