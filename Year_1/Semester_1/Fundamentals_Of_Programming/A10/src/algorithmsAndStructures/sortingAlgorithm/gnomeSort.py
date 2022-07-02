"""
    GNOME SORT

    Gnome Sort also called Stupid sort is based on the concept of a Garden Gnome sorting his flower pots.

    A garden gnome sorts the flower pots by the following method:

        - He looks at the flower pot next to him and the previous one;
          if they are in the right order he steps one pot forward,
          otherwise he swaps them and steps one pot backwards

        - If there is no previous pot (he is at the beginning), he steps forward;
          if there is no pot next to him (he is at the end), he is done

    The time complexity of this algorithm is O(n^2) (even though we do not have any nested loops, but in case
    our list is sorted in reverse order, the gnome will go from i to the beginning n times, for every i from 1 to n);
    If the list is already sorted, the time complexity will be O(n)
"""


from abc import ABC

from src.algorithmsAndStructures.sortingAlgorithm.genericSort import GenericSort


class GnomeSort(GenericSort, ABC):
    def __init__(self, col, comparator):
        super().__init__(col, comparator)

    def sort(self):
        index = 0
        while index < len(self.col):
            if index == 0:
                index += 1
            if self.comparator(self.col[index - 1], self.col[index]):
                index += 1
            else:
                self.col[index], self.col[index - 1] = self.col[index - 1], self.col[index]
                index -= 1
