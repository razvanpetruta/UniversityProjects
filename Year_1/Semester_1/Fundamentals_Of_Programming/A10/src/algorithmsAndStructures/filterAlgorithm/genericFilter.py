class GenericFilter:
    def __init__(self, col, custom_filter=None):
        self.__col = col
        self.__custom_filter = custom_filter
        if self.__custom_filter is None:
            self.__custom_filter = lambda x: True

    @property
    def col(self):
        return self.__col

    @property
    def custom_filter(self):
        return self.__custom_filter

    def filter(self):
        sol = []
        for el in self.col:
            if self.custom_filter(el):
                sol.append(el)
        return sol
