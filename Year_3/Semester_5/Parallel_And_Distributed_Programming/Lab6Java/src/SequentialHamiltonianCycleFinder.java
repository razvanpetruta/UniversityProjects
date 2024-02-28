import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SequentialHamiltonianCycleFinder {
    protected DirectedGraph graph;
    protected boolean solutionFound;

    public SequentialHamiltonianCycleFinder(DirectedGraph graph) {
        this.graph = graph;
        this.solutionFound = false;
    }

    public void beginSearch() {
        ArrayList<Integer> path = new ArrayList<>(Collections.singletonList(0));
        search(0, path);

        if (!solutionFound) {
            System.out.println("No sequential solution");
        }
    }

    protected void search(int currentNode, ArrayList<Integer> path) {
        if (solutionFound) {
            return;
        }

        // we have a solution, from the last node we can get to the first one and all nodes are visited
        if (this.graph.getEdgesForNode(currentNode).contains(0) && path.size() == this.graph.nodesCount() && !solutionFound) {
            solutionFound = true;
            path.add(path.get(0));
            System.out.println("Found a solution: " + path);
            return;
        }

        // all nodes are visited, but we cannot get back to the first node
        if (path.size() == this.graph.nodesCount()) {
            return;
        }

        // go through all vertices
        for (int i = 0; i < this.graph.nodesCount(); i++) {
            // check if we can go to the respective vertex and that vertex is not yet visited
            if (this.graph.getEdgesForNode(currentNode).contains(i) && !checkVisited(i, path)) {
                var pathCopy = new ArrayList<>(path);
                pathCopy.add(i);

                // search recursively for the current node on a new thread
                search(i, pathCopy);
            }
        }
    }

    private boolean checkVisited(int node, List<Integer> path) {
        return path.contains(node);
    }
}