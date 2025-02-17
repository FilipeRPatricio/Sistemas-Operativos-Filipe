import java.util.*;
import java.util.concurrent.*;

/**
 * Representa um caminho no grafo, usado pelo algoritmo para encontrar soluções.
 */
class Path {
    List<Node> nodes = new ArrayList<>();
    boolean finished = false;

    public Path(Node start) {
        nodes.add(start);
    }

    public Node lastNode() {
        return nodes.get(nodes.size() - 1);
    }

    public void addNode(Node node) {
        nodes.add(node);
    }

    public boolean contains(Node node) {
        return nodes.contains(node);
    }
}