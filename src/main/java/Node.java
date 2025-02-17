import java.util.*;
import java.util.concurrent.*;

/**
 * Representa um nó do grafo, possivelmente indicando se é um cruzamento, um cidadão ou um supermercado.
 */
class Node {
    int id;
    List<Node> adjacents = new ArrayList<>();

    public Node(int id) {
        this.id = id;
    }

    public void addAdjacent(Node node) {
        adjacents.add(node);
    }

    public List<Node> getAdjacents() {
        return adjacents;
    }
}