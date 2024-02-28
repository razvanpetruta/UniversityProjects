import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
//        DirectedGraph graph = getOKGraph();
        DirectedGraph graph = new DirectedGraph(1500);
//        System.out.println(graph);

        // Run and time the sequential Hamiltonian cycle finder
        long sequentialDuration = timeSequentialHamiltonianCycleFinder(graph);
        System.out.println("Sequential Time: " + sequentialDuration);

        // Run and time the parallel Hamiltonian cycle finder
        long parallelDuration = timeParallelHamiltonianCycleFinder(graph);
        System.out.println("Parallel Time: " + parallelDuration);
    }

    private static DirectedGraph getOKGraph() {
        List<List<Integer>> edges = new ArrayList<>();
        edges.add(Arrays.asList(1, 2, 3));
        edges.add(Arrays.asList(2, 1));
        edges.add(Arrays.asList(0, 3));
        edges.add(Arrays.asList(1, 4));
        edges.add(Arrays.asList(1, 2, 3, 0));
        return new DirectedGraph(5, edges);
    }

    private static long timeSequentialHamiltonianCycleFinder(DirectedGraph graph) {
        SequentialHamiltonianCycleFinder findHamiltonianCycleSequential = new SequentialHamiltonianCycleFinder(graph);
        Instant startTime = Instant.now();
        findHamiltonianCycleSequential.beginSearch();
        Instant endTime = Instant.now();
        return Duration.between(startTime, endTime).toMillis();
    }

    private static long timeParallelHamiltonianCycleFinder(DirectedGraph graph) throws InterruptedException {
        ParallelHamiltonianCycleFinder findHamiltonianCycleParallel = new ParallelHamiltonianCycleFinder(graph);
        Instant startTime = Instant.now();
        findHamiltonianCycleParallel.beginSearch();
        Instant endTime = Instant.now();
        return Duration.between(startTime, endTime).toMillis();
    }
}
