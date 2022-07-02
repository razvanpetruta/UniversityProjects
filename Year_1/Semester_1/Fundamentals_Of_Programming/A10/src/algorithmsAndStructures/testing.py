import unittest

from algorithmsAndStructures.container.containerClass import Container
from src.algorithmsAndStructures.filterAlgorithm.genericFilter import GenericFilter
from src.algorithmsAndStructures.sortingAlgorithm.sorting import Sorting
from src.domain.bookClass import Book


class SortingTest(unittest.TestCase):
    def test_ascending_integers(self):
        col = [2, 5, 4, 3, 1, 9, 7]
        Sorting.sort(col)
        self.assertEqual(col, [1, 2, 3, 4, 5, 7, 9])

    def test_descending_order(self):
        def descending_order(a, b):
            return a > b

        col = [2, 5, 4, 3, 1, 9, 7]
        Sorting.sort(col, descending_order)
        self.assertEqual(col, [9, 7, 5, 4, 3, 2, 1])

    def test_sort_books_after_id(self):
        def id_ascending(b1, b2):
            return b1.book_id <= b2.book_id

        def id_descending(b1, b2):
            return b1.book_id >= b2.book_id

        book1 = Book(3, "aa", "aa")
        book2 = Book(1, "bb", "bb")
        book3 = Book(5, "cc", "cc")
        book4 = Book(2, "dd", "dd")
        col = [book1, book2, book3, book4]
        Sorting.sort(col, id_ascending)
        self.assertEqual(col, [book2, book4, book1, book3])
        Sorting.sort(col, id_descending)
        self.assertEqual(col, [book3, book1, book4, book2])

    def test_custom_sort_books(self):
        """
            Sort ascending after title and descending after author.
        """

        def custom_comparator(b1, b2):
            if b1.title == b2.title:
                return b1.author >= b2.author
            return b1.title < b2.title

        book1 = Book(3, "cc", "aa")
        book2 = Book(1, "dd", "bb")
        book3 = Book(5, "cc", "cc")
        book4 = Book(2, "bb", "dd")
        col = [book1, book2, book3, book4]
        Sorting.sort(col, custom_comparator)
        self.assertEqual(col, [book4, book3, book1, book2])

    def test_custom_class(self):
        """
            Sort ascending after the first field, descending after the second field and ascending after the third field.
        """

        class Custom:
            def __init__(self, nr1, nr2, nr3):
                self.nr1 = nr1
                self.nr2 = nr2
                self.nr3 = nr3

        def custom_comparator(a, b):
            if a.nr1 == b.nr1:
                if a.nr2 == b.nr2:
                    return a.nr3 <= b.nr3
                return a.nr2 >= b.nr2
            return a.nr1 <= b.nr1

        ob1 = Custom(1, 2, 3)
        ob2 = Custom(1, 2, 4)
        ob3 = Custom(1, 2, 0)
        ob4 = Custom(1, 3, 1)
        ob5 = Custom(1, 3, 0)
        ob6 = Custom(2, 2, 2)
        col = [ob1, ob2, ob3, ob4, ob5, ob6]
        Sorting.sort(col, custom_comparator)
        self.assertEqual(col, [ob5, ob4, ob3, ob1, ob2, ob6])


class FilteringTest(unittest.TestCase):
    def test_filter_nothing(self):
        col = [1, 2, 3, 4, 5]
        self.assertEqual(GenericFilter(col).filter(), [1, 2, 3, 4, 5])

    def test_greater_than_0(self):
        y = 0

        def greater_than_0(x):
            return x >= y

        col = [1, -1, 2, -2, 3, -3]
        self.assertEqual(GenericFilter(col, greater_than_0).filter(), [1, 2, 3])

    def test_filter_books_with_title_starting_with_the(self):
        def custom_filter(book):
            return book.title.lower().startswith("the")

        book1 = Book(3, "The wall", "aa")
        book2 = Book(1, "dd", "bb")
        book3 = Book(5, "The love", "cc")
        book4 = Book(2, "bb", "dd")
        col = [book1, book2, book3, book4]
        self.assertEqual(GenericFilter(col, custom_filter).filter(), [book1, book3])


class ContainerTest(unittest.TestCase):
    def test_container(self):
        c = Container()
        c["1"] = 2
        self.assertEqual(c["1"], 2)
        with self.assertRaises(IndexError) as ie:
            c["2"] += 1
        c["2"] = 4
        del c["2"]
        with self.assertRaises(IndexError) as ie:
            del c["2"]
        self.assertEqual(len(c), 1)
        i = 0
        for el in c:
            i += 1
        self.assertEqual(i, 1)

