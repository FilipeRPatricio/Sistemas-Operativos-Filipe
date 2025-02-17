import java.util.*;
import java.util.concurrent.*;

/**
 * Representa um nó do grafo, possivelmente indicando se é um cruzamento, um cidadão ou um supermercado.
 */
class Node {
    int id;
    List<Node> adjacents = new ArrayList<>();
    NodeType nodeType;

    enum NodeType {
        INTERSECTION, CITIZEN, SUPERMARKET
    }

    public Node(int id, NodeType type) {
        this.id = id;
        this.nodeType = type;
    }

    public void addAdjacent(Node node) {
        adjacents.add(node);
    }

    public List<Node> getAdjacents() {
        return adjacents;
    }

    public NodeType getType(){
        return nodeType;
    }
    public void setType(NodeType type) {
        this.nodeType = type;
    }
}