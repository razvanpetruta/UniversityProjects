from abc import ABC, abstractmethod


class GenericSort(ABC):
    def __init__(self, col, comparator):
        self.__col = col
        self.__comparator = comparator
        if self.__comparator is None:
            self.__comparator = lambda x, y: x < y

    @property
    def col(self):
        return self.__col

    @property
    def comparator(self):
        return self.__comparator

    @abstractmethod
    def sort(self):
        pass
