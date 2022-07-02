import random

from graphRepresentation.graph import Graph, read_from_file


class Console:
    def __init__(self):
        self.__graph = Graph(0, 0)

    def generate_random_graph(self):
        no_of_vertices = int(input("number of vertices: "))
        no_of_edges = int(input("number of edges: "))
        if no_of_edges > no_of_vertices * (no_of_vertices + 1) / 2:
            self.__graph = Graph(0, 0)
            raise ValueError(f"the maximum number of edges in a graph with {no_of_vertices} vertices is "
                             f"{no_of_vertices * no_of_vertices}")
        graph = Graph(no_of_vertices, 0)
        graph.initialise_in_and_out_for_random()
        while graph.no_of_edges < no_of_edges:
            x = random.randint(0, no_of_vertices - 1)
            y = random.randint(0, no_of_vertices - 1)
            if x == y:
                continue
            if not graph.check_edge(x, y):
                graph.add_edge(x, y)
        self.__graph = graph

    def get_graph_from_file(self):
        path = input("path to the file: ")
        self.__graph = read_from_file(path)

    def get_number_of_vertices(self):
        print(f"number of vertices: {self.__graph.no_of_vertices}")

    def get_number_of_edges(self):
        print(f"number of edges: {self.__graph.no_of_edges}")

    def get_the_vertices(self):
        for vertex in self.__graph.parse_vertices():
            print(vertex, end=" ")
        print()

    @staticmethod
    def read_edge():
        print("edge x - y")
        x = int(input("x: "))
        y = int(input("y: "))
        return x, y

    def test_edge(self):
        x, y = Console.read_edge()
        if self.__graph.check_edge(x, y):
            print(f"{x} - {y} exists")
        else:
            print("edge not found")

    def get_degree_of_vertex(self):
        vertex = int(input("vertex: "))
        print(f"in degree of {vertex}: {self.__graph.get_degree(vertex)}")

    def get_edges(self):
        vertex = int(input("vertex: "))
        for x in self.__graph.parse_edges(vertex):
            print(f"({x}, {vertex})", end=" ")
        print()

    def add_vertex_to_graph(self):
        vertex = int(input("new vertex: "))
        self.__graph.add_vertex(vertex)

    def remove_vertex_from_graph(self):
        vertex = int(input("vertex to remove: "))
        self.__graph.remove_vertex(vertex)

    def add_edge_to_graph(self):
        x, y = Console.read_edge()
        self.__graph.add_edge(x, y)

    def remove_edge_from_graph(self):
        x, y = Console.read_edge()
        self.__graph.remove_edge(x, y)

    def get_copy(self):
        return self.__graph.make_copy()

    def parse_graph(self):
        for x in sorted(self.__graph.parse_vertices()):
            print(f"{x}: ", end="")
            for y in sorted(self.__graph.parse_edges(x)):
                print(f"{y} ", end="")
            print()

    def compute_maximum_cliques(self):
        self.__graph.maximum_clique()

    @staticmethod
    def print_menu():
        print("\n"
              "0. EXIT\n"
              "1. Generate a random graph\n"
              "2. Read the graph from a file\n"
              "3. Get the number of vertices\n"
              "4. Get the number of edges\n"
              "5. Get the degree of a vertex\n"
              "6. List the edges of a vertex\n"
              "7. List the vertices\n"
              "8. Edge information\n"
              "9. Add vertex\n"
              "10. Remove vertex\n"
              "11. Add edge\n"
              "12. Remove edge\n"
              "13. Parse graph\n"
              "14. Compute maximum cliques\n")

    @staticmethod
    def read_command():
        command = int(input("command: "))
        return command

    def start(self):
        print("Initially the graph contains nothing...")
        commands = {
            1: self.generate_random_graph,
            2: self.get_graph_from_file,
            3: self.get_number_of_vertices,
            4: self.get_number_of_edges,
            5: self.get_degree_of_vertex,
            6: self.get_edges,
            7: self.get_the_vertices,
            8: self.test_edge,
            9: self.add_vertex_to_graph,
            10: self.remove_vertex_from_graph,
            11: self.add_edge_to_graph,
            12: self.remove_edge_from_graph,
            13: self.parse_graph,
            14: self.compute_maximum_cliques
        }
        while True:
            try:
                Console.print_menu()
                command = Console.read_command()
                if command == 0:
                    return
                commands[command]()
            except Exception as e:
                print(str(e))
