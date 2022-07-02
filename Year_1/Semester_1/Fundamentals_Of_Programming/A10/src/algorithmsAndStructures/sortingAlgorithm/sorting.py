from src.algorithmsAndStructures.sortingAlgorithm.gnomeSort import GnomeSort


class Sorting:
    @staticmethod
    def sort(col, comparator=None, algorithm=GnomeSort):
        sorting_algorithm = algorithm(col, comparator)
        sorting_algorithm.sort()
