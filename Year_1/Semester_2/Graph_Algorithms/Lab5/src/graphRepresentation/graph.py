import copy


class Graph:
    def __init__(self, no_of_vertices=0, no_of_edges=0):
        self.__no_of_vertices = no_of_vertices
        self.__no_of_edges = no_of_edges
        self.__dictionary = {}

    def initialise_in_and_out_for_random(self):
        for i in range(self.__no_of_vertices):
            self.__dictionary[i] = []

    @property
    def no_of_edges(self):
        return self.__no_of_edges

    @property
    def no_of_vertices(self):
        return self.__no_of_vertices

    @property
    def dictionary(self):
        return self.__dictionary

    def valid_vertex(self, x):
        return x in self.__dictionary

    def check_edge(self, x, y):
        return self.valid_vertex(x) and self.valid_vertex(y) and (x in self.__dictionary[y])

    def get_degree(self, x):
        if not self.valid_vertex(x):
            raise ValueError("Vertex not found")
        return len(self.__dictionary[x])

    def parse_vertices(self):
        sol = []
        for vertex in sorted(list(self.__dictionary)):
            sol.append(vertex)
        return sol

    def parse_edges(self, x):
        if not self.valid_vertex(x):
            raise ValueError("Vertex not found")
        sol = []
        for y in self.__dictionary[x]:
            sol.append(y)
        return sol

    def add_vertex(self, x):
        if self.valid_vertex(x):
            raise ValueError("The vertex already exists")
        self.__dictionary[x] = []
        self.__no_of_vertices += 1

    def add_vertex_from_file(self, x):
        self.__dictionary[x] = []

    def remove_vertex(self, x):
        if not self.valid_vertex(x):
            raise ValueError("The vertex doesn't belong to the graph")
        # remove x as a neighbour
        for y in self.__dictionary[x]:
            self.__dictionary[y].remove(x)
            self.__no_of_edges -= 1
        self.__dictionary.pop(x)
        self.__no_of_vertices -= 1

    def add_edge(self, x, y):
        # check if x and y are valid vertices
        if not self.valid_vertex(x) or not self.valid_vertex(y):
            raise ValueError("Vertex not found")
        # check if the edge doesn't already exist
        if self.check_edge(x, y):
            raise ValueError("The edge already exists")
        self.__dictionary[x].append(y)
        self.__dictionary[y].append(x)
        self.__no_of_edges += 1

    def add_edge_from_file(self, x, y):
        if not self.valid_vertex(x):
            self.add_vertex_from_file(x)
        if not self.valid_vertex(y):
            self.add_vertex_from_file(y)
        self.__dictionary[y].append(x)
        self.__dictionary[x].append(y)

    def remove_edge(self, x, y):
        # check if the edge exists
        if self.check_edge(x, y) is None:
            raise ValueError("The edge doesn't exist")
        self.__dictionary[y].remove(x)
        self.__dictionary[x].remove(y)
        self.__no_of_edges -= 1

    def make_copy(self):
        return copy.deepcopy(self)

    def bron_kebosch(self, temporary_result, candidates, excluded_set, maximum_clique):
        if len(candidates) == 0 and len(excluded_set) == 0:
            print(temporary_result)
            if len(temporary_result) > len(maximum_clique):
                maximum_clique.clear()
                maximum_clique.update(temporary_result)
        for vertex in candidates.copy():
            # add the vertex to the temporary result
            temporary_result.add(vertex)
            # remove the non-neighbours from the candidates (prepare the new candidates list for the next call)
            remaining_vertices_candidates = set()
            for neighbour in self.parse_edges(vertex):
                if neighbour in candidates:
                    remaining_vertices_candidates.add(neighbour)
            # remove the non-neighbours from the excluded set
            remaining_vertices_excluded_set = set()
            for neighbour in self.parse_edges(vertex):
                if neighbour in excluded_set:
                    remaining_vertices_excluded_set.add(neighbour)
            self.bron_kebosch(temporary_result, remaining_vertices_candidates, remaining_vertices_excluded_set, maximum_clique)
            # after the recursive call, remove the vertex from the candidates and add it to the excluded set
            candidates.remove(vertex)
            excluded_set.add(vertex)
            temporary_result.remove(vertex)

    def maximum_clique(self):
        temporary_result = set()
        candidates = set()
        excluded_set = set()
        maximum_clique = set()
        for vertex in self.parse_vertices():
            candidates.add(vertex)
        print("Maximal cliques: ")
        self.bron_kebosch(temporary_result, candidates, excluded_set, maximum_clique)
        print("One maximum clique: ", maximum_clique)


def read_from_file(path):
    with open(path, "r") as file_pointer:
        lines = file_pointer.readlines()
        if lines[0] == "The graph is empty or couldn't be generated":
            return Graph(0, 0)
        first_line_tokens = lines[0].split()
        no_of_vertices = int(first_line_tokens[0].strip())
        no_of_edges = int(first_line_tokens[1].strip())
        graph = Graph(no_of_vertices, no_of_edges)
        graph.initialise_in_and_out_for_random()
        # now add the edges
        for i in range(1, len(lines)):
            line_tokens = lines[i].split()
            if len(line_tokens) == 0:
                break
            graph.add_edge_from_file(int(line_tokens[0].strip()), int(line_tokens[1].strip()))
        return graph
