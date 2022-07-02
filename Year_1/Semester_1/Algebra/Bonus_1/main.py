"""
Input: non-zero natural number n
Output: 
    1. the number of partitions on a set A = {a1, a2, ..., an}
    2. the partitions on a set A = {a1, a2, ..., an} and their corresponding equivalence relations
"""


def bell_number(n):
    """
     1
     1 2
     2 3 5
     5 7 10 15
     15 20 27 37 52
     . . . . . . . .
    :param n: the numbers of elements
    :return: the number of partitions
    """
    bell = []
    for i in range(n + 1):
        row = [0] * (n + 1)
        bell.append(row)

    bell[0][0] = 1
    for i in range(1, n + 1):
        # last element from the previous line
        bell[i][0] = bell[i - 1][i - 1]
        # fill the remaining elements of the line
        for j in range(1, i + 1):
            bell[i][j] = bell[i - 1][j - 1] + bell[i][j - 1]

    return bell[n][0]


def partition(my_set):
    """
    The problem is solved recursively. If you have a partition of n - 1 elements and want to add one,
    you either place the nth element in an existing subset, or add it as a single subset.
    :param my_set: our set
    :return: a list of lists, each one containing a partition
    """
    if len(my_set) == 1:
        yield [my_set]
        return

    first = my_set[0]
    for smaller in partition(my_set[1:]):
        # insert 'first' in each of the subpartition's subsets
        for n, subset in enumerate(smaller):
            yield smaller[:n] + [[first] + subset] + smaller[n + 1:]
        # put 'first' in its own subset
        yield [[first]] + smaller


def equivalence_relations(part, n):
    """
    In order to get the equivalence relations, first we add the pairs (ai, ai) and then
    we check if in the partition we have subsets with more elements, in which case we add
    what must be added. ex: [a1, a2] we add (a1, a2) and (a2, a1)
    :param part: the partition
    :param n: the number of elements
    :return: the equivalence relations
    """
    eq_rel = []
    for i in range(1, n + 1):
        eq_rel.append([i, i])
    for subset in part:
        for i in range(0, len(subset) - 1):
            for j in range(i + 1, len(subset)):
                eq_rel.append([subset[i], subset[j]])
                eq_rel.append([subset[j], subset[i]])
    return eq_rel


def print_set(my_set, g):
    """
    We print the list containing the set in a nicer way.
    :param my_set: the set
    """
    g.write("{")
    for subset in my_set:
        g.write("{")
        for i in range(len(subset) - 1):
            g.write("a" + str(subset[i]) + ", ")
        g.write("a" + str(subset[-1]) + "}")
        if subset != my_set[-1]:
            g.write(", ")
    g.write("}")


def print_pairs(my_set, g):
    """
    We print the list containing the set in a nicer way.
    :param my_set: the set
    """
    g.write("{")
    for subset in my_set:
        g.write("(")
        for i in range(len(subset) - 1):
            g.write("a" + str(subset[i]) + ", ")
        g.write("a" + str(subset[-1]) + ")")
        if subset != my_set[-1]:
            g.write(", ")
    g.write("}")


def partitions_and_relations(n, g):
    """
    For every partition generated, we compute the set of equivalence relations and we print the info.
    :param n: number of elements
    """
    my_set = list(range(1, n + 1))
    partitions = partition(my_set)
    for part in partitions:
        part = sorted(part)
        print_set(part, g)
        g.write(" -> ")
        eq_rel = equivalence_relations(part, n)
        print_pairs(eq_rel, g)
        g.write("\n")


if __name__ == "__main__":
    for i in range(1, 6):
        f = open("test" + str(i) + "input.txt", "r")
        g = open("test" + str(i) + "output.txt", "w")
        n = int(f.read())
        f.close()
        g.write("The number of partitions of a set with " + str(n) + " elements is: " + str(bell_number(n)) + '\n')
        g.write("\nThe partitions of the set and their corresponding equivalence relations are: \n")
        partitions_and_relations(n, g)
        g.close()
