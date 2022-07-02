import copy


class Graph:
    def __init__(self, no_of_vertices=0, no_of_edges=0):
        self.__no_of_vertices = no_of_vertices
        self.__no_of_edges = no_of_edges
        self.__dictionary_in = {}
        self.__dictionary_out = {}
        self.__dictionary_cost = {}

    def initialise_in_and_out_for_random(self):
        for i in range(self.__no_of_vertices):
            self.__dictionary_in[i] = []
            self.__dictionary_out[i] = []

    @property
    def no_of_edges(self):
        return self.__no_of_edges

    @property
    def no_of_vertices(self):
        return self.__no_of_vertices

    @property
    def dictionary_in(self):
        return self.__dictionary_in

    @property
    def dictionary_out(self):
        return self.__dictionary_out

    @property
    def dictionary_cost(self):
        return self.__dictionary_cost

    def valid_vertex(self, x):
        return x in self.__dictionary_in and x in self.__dictionary_out

    def check_edge(self, x, y):
        if (x, y) in self.__dictionary_cost:
            return self.__dictionary_cost[(x, y)]
        return None

    def get_in_degree(self, x):
        if not self.valid_vertex(x):
            raise ValueError("Vertex not found")
        return len(self.__dictionary_in[x])

    def get_out_degree(self, x):
        if not self.valid_vertex(x):
            raise ValueError("Vertex not found")
        return len(self.__dictionary_out[x])

    def parse_vertices(self):
        for vertex in sorted(list(self.__dictionary_in)):
            yield vertex

    def parse_inbound_edges(self, x):
        if not self.valid_vertex(x):
            raise ValueError("Vertex not found")
        for y in self.__dictionary_in[x]:
            yield y

    def parse_outbound_edges(self, x):
        if not self.valid_vertex(x):
            raise ValueError("Vertex not found")
        for y in self.__dictionary_out[x]:
            yield y

    def get_cost(self, x, y):
        if self.check_edge(x, y) is None:
            raise ValueError("Edge not found")
        return self.__dictionary_cost[(x, y)]

    def change_cost(self, x, y, new_cost):
        if self.check_edge(x, y) is None:
            raise ValueError("Edge not found")
        self.__dictionary_cost[(x, y)] = new_cost

    def add_vertex(self, x):
        if self.valid_vertex(x):
            raise ValueError("The vertex already exists")
        # initialise the inbound and outbound neighbours
        self.__dictionary_in[x] = []
        self.__dictionary_out[x] = []
        self.__no_of_vertices += 1

    def add_vertex_from_file(self, x):
        self.__dictionary_in[x] = []
        self.__dictionary_out[x] = []

    def remove_vertex(self, x):
        if not self.valid_vertex(x):
            raise ValueError("The vertex doesn't belong to the graph")
        # remove x as a neighbour
        for y in self.__dictionary_in[x]:
            self.__dictionary_out[y].remove(x)
            self.__dictionary_cost.pop((y, x))
            self.__no_of_edges -= 1
        for y in self.__dictionary_out[x]:
            self.__dictionary_in[y].remove(x)
            self.__dictionary_cost.pop((x, y))
            self.__no_of_edges -= 1
        # pop x from inbound and outbound neighbours
        self.__dictionary_in.pop(x)
        self.__dictionary_out.pop(x)
        self.__no_of_vertices -= 1

    def add_edge(self, x, y, cost):
        # check if x and y are valid vertices
        if not self.valid_vertex(x) or not self.valid_vertex(y):
            raise ValueError("Vertex not found")
        # check if the edge doesn't already exist
        if self.check_edge(x, y) is not None:
            raise ValueError("The edge already exists")
        self.__dictionary_in[y].append(x)
        self.__dictionary_out[x].append(y)
        self.__dictionary_cost[(x, y)] = cost
        self.__no_of_edges += 1

    def add_edge_from_file(self, x, y, cost):
        if not self.valid_vertex(x):
            self.add_vertex_from_file(x)
        if not self.valid_vertex(y):
            self.add_vertex_from_file(y)
        self.__dictionary_in[y].append(x)
        self.__dictionary_out[x].append(y)
        self.__dictionary_cost[(x, y)] = cost

    def remove_edge(self, x, y):
        # check if the edge exists
        if self.check_edge(x, y) is None:
            raise ValueError("The edge doesn't exist")
        self.__dictionary_in[y].remove(x)
        self.__dictionary_out[x].remove(y)
        self.__dictionary_cost.pop((x, y))
        self.__no_of_edges -= 1

    def make_copy(self):
        return copy.deepcopy(self)


def read_from_file(path):
    with open(path, "r") as file_pointer:
        lines = file_pointer.readlines()
        if lines[0] == "The graph is empty or couldn't be generated":
            return Graph(0, 0)
        first_line_tokens = lines[0].split()
        no_of_vertices = int(first_line_tokens[0].strip())
        no_of_edges = int(first_line_tokens[1].strip())
        graph = Graph(no_of_vertices, no_of_edges)
        # now add the edges
        for i in range(1, len(lines)):
            line_tokens = lines[i].split()
            if len(line_tokens) == 0:
                break
            if len(line_tokens) == 1:
                graph.add_vertex_from_file(int(line_tokens[0].strip()))
            else:
                graph.add_edge_from_file(int(line_tokens[0].strip()), int(line_tokens[1].strip()),
                                         int(line_tokens[2].strip()))
        return graph


def write_to_file(graph, path):
    with open(path, "w") as file_pointer:
        if graph.no_of_vertices == 0 and graph.no_of_edges == 0:
            file_pointer.write("The graph is empty or couldn't be generated")
            return
        file_pointer.write(f"{graph.no_of_vertices} {graph.no_of_edges}\n")
        # write isolated vertices
        for x in graph.parse_vertices():
            if graph.get_in_degree(x) == 0 and graph.get_out_degree(x) == 0:
                file_pointer.write(f"{x}\n")
        for edge in graph.dictionary_cost:
            file_pointer.write(f"{edge[0]} {edge[1]} {graph.dictionary_cost[edge]}\n")
