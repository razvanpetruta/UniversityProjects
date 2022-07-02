import copy


class UndirectedGraph:
    def __init__(self, no_of_vertices=0, no_of_edges=0):
        self.__no_of_vertices = no_of_vertices
        self.__no_of_edges = no_of_edges
        self.__dictionary_bound = {}

    def initialise_bound_for_random(self):
        for i in range(self.__no_of_vertices):
            self.__dictionary_bound[i] = []

    @property
    def no_of_edges(self):
        return self.__no_of_edges

    @property
    def no_of_vertices(self):
        return self.__no_of_vertices

    @property
    def dictionary_bound(self):
        return self.__dictionary_bound

    def valid_vertex(self, x):
        return x in self.__dictionary_bound

    def check_edge(self, x, y):
        return x in self.__dictionary_bound[y] and y in self.__dictionary_bound[x]

    def get_degree(self, x):
        if not self.valid_vertex(x):
            raise ValueError("Vertex not found")
        return len(self.__dictionary_bound[x])

    def parse_vertices(self):
        vertices = []
        for vertex in sorted(list(self.__dictionary_bound)):
            vertices.append(vertex)
        return vertices

    def parse_edges(self, x):
        if not self.valid_vertex(x):
            raise ValueError("Vertex not found")
        neighbours = []
        for y in self.__dictionary_bound[x]:
            neighbours.append(y)
        return neighbours

    def add_vertex(self, x):
        if self.valid_vertex(x):
            raise ValueError("The vertex already exists")
        # initialise the inbound and outbound neighbours
        self.__dictionary_bound[x] = []
        self.__no_of_vertices += 1

    def add_vertex_from_file(self, x):
        self.__dictionary_bound[x] = []

    def remove_vertex(self, x):
        if not self.valid_vertex(x):
            raise ValueError("The vertex doesn't belong to the graph")
        # remove x as a neighbour
        for y in self.__dictionary_bound[x]:
            self.__dictionary_bound[y].remove(x)
            self.__no_of_edges -= 1
        # pop x from bound neighbours
        self.__dictionary_bound.pop(x)
        self.__no_of_vertices -= 1

    def add_edge(self, x, y):
        # check if x and y are valid vertices
        if not self.valid_vertex(x) or not self.valid_vertex(y):
            raise ValueError("Vertex not found")
        # check if the edge doesn't already exist
        if self.check_edge(x, y):
            raise ValueError("The edge already exists")
        self.__dictionary_bound[x].append(y)
        self.__dictionary_bound[y].append(x)
        self.__no_of_edges += 1

    def add_edge_from_file(self, x, y):
        if not self.valid_vertex(x):
            self.add_vertex_from_file(x)
        if not self.valid_vertex(y):
            self.add_vertex_from_file(y)
        if x in self.__dictionary_bound[y] and y in self.__dictionary_bound[x]:
            return
        self.__dictionary_bound[x].append(y)
        self.__dictionary_bound[y].append(x)

    def remove_edge(self, x, y):
        # check if the edge exists
        if not self.check_edge(x, y):
            raise ValueError("The edge doesn't exist")
        self.__dictionary_bound[y].remove(x)
        self.__dictionary_bound[x].remove(y)
        self.__no_of_edges -= 1

    def make_copy(self):
        return copy.deepcopy(self)

    def depth_first_traversal(self, x, visited):
        # store the vertices representing the connected component
        connected = []
        # we use a stack in order to parse the graph using DFS
        stack = [x]
        while len(stack) > 0:
            # pop the vertex from the top of the stack
            current_vertex = stack.pop()
            if not visited[current_vertex]:
                # add the vertex to the current connected component
                connected.append(current_vertex)
                # parse through the neighbours of the current vertex
                for y in self.parse_edges(current_vertex):
                    if not visited[y]:
                        stack.append(y)
                # mark the current vertex as visited
                visited[current_vertex] = True
        return connected

    def find_connected_components(self):
        # a list containing the connected components as graph objects
        all_components = []
        # mark the visited vertices
        visited = {}
        for x in self.parse_vertices():
            visited[x] = False
        # look for vertices that were not visited
        for x in self.parse_vertices():
            if not visited[x]:
                # get the vertices of the current component
                component = self.depth_first_traversal(x, visited)
                # create a new graph object for the current component
                graph = UndirectedGraph()
                # we need to store the edges in order to not add them twice
                edges = []
                for vertex in component:
                    # add the vertex from the current component
                    graph.add_vertex(vertex)
                    # add to the edges list all the edges from the connected component
                    for y in self.parse_edges(vertex):
                        if (vertex, y) not in edges and (y, vertex) not in edges:
                            edges.append((vertex, y))
                # now, just add the edges to the graph
                for edge in edges:
                    graph.add_edge(edge[0], edge[1])
                # append the graph to our list containing all the connected components
                all_components.append(graph)
        return all_components


def read_from_file(path):
    with open(path, "r") as file_pointer:
        lines = file_pointer.readlines()
        if lines[0] == "The graph is empty or couldn't be generated":
            return UndirectedGraph(0, 0)
        first_line_tokens = lines[0].split()
        no_of_vertices = int(first_line_tokens[0].strip())
        no_of_edges = int(first_line_tokens[1].strip())
        graph = UndirectedGraph(no_of_vertices, no_of_edges)
        graph.initialise_bound_for_random()
        # now add the edges
        for i in range(1, len(lines)):
            line_tokens = lines[i].split()
            if len(line_tokens) == 0:
                break
            if len(line_tokens) == 1:
                graph.add_vertex_from_file(int(line_tokens[0].strip()))
            else:
                graph.add_edge_from_file(int(line_tokens[0].strip()), int(line_tokens[1].strip()))
        return graph


def write_to_file(graph, path):
    with open(path, "w") as file_pointer:
        if graph.no_of_vertices == 0 and graph.no_of_edges == 0:
            file_pointer.write("The graph is empty or couldn't be generated")
            return
        file_pointer.write(f"{graph.no_of_vertices} {graph.no_of_edges}\n")
        edges = []
        # write isolated vertices
        for x in graph.parse_vertices():
            if graph.get_degree(x) == 0:
                file_pointer.write(f"{x}\n")
            else:
                for y in graph.parse_edges(x):
                    if (x, y) not in edges and (y, x) not in edges:
                        edges.append((x, y))
        # write the edges to the file
        for edge in edges:
            file_pointer.write(f"{edge[0]} {edge[1]}\n")
