import java.util.*;

public class DirectedGraph {
    private List<Integer> nodes;
    private List<List<Integer>> edges;

    public DirectedGraph(int nodeCount) {
        this.nodes = new ArrayList<>();
        for (int i = 0; i < nodeCount; i++) {
            this.nodes.add(i);
        }
        this.generateEdges();
    }

    public DirectedGraph(int nodeCount, List<List<Integer>> edges) {
        this.nodes = new ArrayList<>();
        for (int i = 0; i < nodeCount; i++) {
            this.nodes.add(i);
        }
        this.edges = edges;
    }

    public List<Integer> getNodes() {
        return this.nodes;
    }

    public List<List<Integer>> getEdges() {
        return this.edges;
    }

    public List<Integer> getEdgesForNode(Integer node) {
        return this.edges.get(node);
    }

    public Integer nodesCount() {
        return this.nodes.size();
    }

    @Override
    public String toString() {
        return "Graph { \n" +
                "\tnodes = " + this.nodes + "\n" +
                "\tedges = " + this.edges + "\n}";
    }

    private void addEdge(Integer node1, Integer node2) {
        if (Objects.equals(node1, node2) || this.edges.get(node1).contains(node2)) {
            return;
        }

        this.edges.get(node1).add(node2);
    }

    private void generateEdges() {
        this.edges = new ArrayList<>();
        for (Integer node: nodes) {
            this.edges.add(new ArrayList<>());
        }

        Random random = new Random();
        Integer size = (int) Math.pow(this.nodes.size(), 2);
        for (int i = 0; i < size / 2; i++) {
            Integer node1 = random.nextInt(this.nodes.size());
            Integer node2 = random.nextInt(this.nodes.size());
            this.addEdge(node1, node2);
        }
    }
}
