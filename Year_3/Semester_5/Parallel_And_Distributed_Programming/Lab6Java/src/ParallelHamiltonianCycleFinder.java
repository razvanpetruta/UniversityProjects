import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class ParallelHamiltonianCycleFinder {
    public DirectedGraph graph;
    private final AtomicBoolean solutionFound;

    public ParallelHamiltonianCycleFinder(DirectedGraph graph) {
        this.graph = graph;
        this.solutionFound = new AtomicBoolean(false);
    }

    public void beginSearch() throws InterruptedException {
        var executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(8);

        List<Integer> neighbors = this.graph.getEdgesForNode(0);
        int batchSize = Math.max((int) Math.ceil(neighbors.size() / 8), 1);

        for (int i = 0; i < neighbors.size(); i += batchSize) {
            int start = i;
            int end = Math.min(i + batchSize, neighbors.size());
            final Runnable task = () -> {
                try {
                    for (int j = start; j < end; j++) {
                        int node = neighbors.get(j);
                        ArrayList<Integer> path = new ArrayList<>(Collections.singletonList(0));
                        path.add(node);
                        search(node, path);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            };
            executor.execute(task);
        }

        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);

        if (!solutionFound.get()) {
            System.out.println("No parallel solution");
        }
    }


    private void search(int currentNode, ArrayList<Integer> path) {
        if (solutionFound.get()) {
            return;
        }

        // we have a solution, from the last node we can get to the first one and all nodes are visited
        if (this.graph.getEdgesForNode(currentNode).contains(0) && path.size() == this.graph.nodesCount() && !solutionFound.get()) {
            solutionFound.set(true);
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
            if (solutionFound.get()) {
                return;
            }

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