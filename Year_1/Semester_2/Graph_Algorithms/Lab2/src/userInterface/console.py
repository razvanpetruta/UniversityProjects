import random

from src.graphRepresentation.undirectedgraph import UndirectedGraph, read_from_file, write_to_file


class Console:
    def __init__(self):
        self.__graph = UndirectedGraph(0, 0)

    def generate_random_graph(self):
        no_of_vertices = int(input("number of vertices: "))
        no_of_edges = int(input("number of edges: "))
        if no_of_edges > no_of_vertices * (no_of_vertices - 1) / 2:
            self.__graph = UndirectedGraph(0, 0)
            raise ValueError(f"the maximum number of edges in a graph with {no_of_vertices} vertices is "
                             f"{no_of_vertices * (no_of_vertices - 1) / 2}")
        graph = UndirectedGraph(no_of_vertices, 0)
        graph.initialise_bound_for_random()
        while graph.no_of_edges < no_of_edges:
            x = random.randint(0, no_of_vertices - 1)
            y = random.randint(0, no_of_vertices - 1)
            if x != y and not graph.check_edge(x, y):
                graph.add_edge(x, y)
        self.__graph = graph

    def get_graph_from_file(self):
        path = input("path to the file: ")
        self.__graph = read_from_file(path)

    def push_graph_to_file(self):
        path = input("path to the file: ")
        write_to_file(self.__graph, path)

    def get_number_of_vertices(self):
        print(f"number of vertices: {self.__graph.no_of_vertices}")

    def get_number_of_edges(self):
        print(f"number of edges: {self.__graph.no_of_edges}")

    def get_vertices(self):
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
            print(f"{x} - {y} found")
        else:
            print(f"{x} - {y} not found")

    def get_degree_of_vertex(self):
        vertex = int(input("vertex: "))
        print(f"in degree of {vertex}: {self.__graph.get_degree(vertex)}")

    def get_edges(self):
        x = int(input("vertex: "))
        for y in sorted(self.__graph.parse_edges(x)):
            print(f"({x}, {y})", end=" ")
        print()

    def parse_graph(self):
        for x in sorted(self.__graph.parse_vertices()):
            print(f"{x}: ", end="")
            for y in sorted(self.__graph.parse_edges(x)):
                print(f"{y} ", end="")
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

    def get_all_connected_components(self):
        i = 1
        for component in self.__graph.find_connected_components():
            print(f"component {i}")
            for x in sorted(component.parse_vertices()):
                print(f"{x}: ", end="")
                for y in sorted(component.parse_edges(x)):
                    print(f"{y} ", end="")
                print()
            print()
            i += 1

    @staticmethod
    def print_menu():
        print("\n"
              "0. EXIT\n"
              "1. Generate a random graph\n"
              "2. Read the graph from a file\n"
              "3. Write the graph to a file\n"
              "4. Get the number of vertices\n"
              "5. Get the number of edges\n"
              "6. Get the degree of a vertex\n"
              "7. List the edges of a vertex\n"
              "8. List the vertices\n"
              "9. Check edge\n"
              "10. Add vertex\n"
              "11. Remove vertex\n"
              "12. Add edge\n"
              "13. Remove edge\n"
              "14. Parse graph\n"
              "15. Get the connected components (using DFS)\n")

    @staticmethod
    def read_command():
        command = int(input("command: "))
        return command

    def start(self):
        print("Initially the graph contains nothing...")
        commands = {
            1: self.generate_random_graph,
            2: self.get_graph_from_file,
            3: self.push_graph_to_file,
            4: self.get_number_of_vertices,
            5: self.get_number_of_edges,
            6: self.get_degree_of_vertex,
            7: self.get_edges,
            8: self.get_vertices(),
            9: self.test_edge,
            10: self.add_vertex_to_graph,
            11: self.remove_vertex_from_graph,
            12: self.add_edge_to_graph,
            13: self.remove_edge_from_graph,
            14: self.parse_graph,
            15: self.get_all_connected_components
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
