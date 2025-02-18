import java.util.*;
import java.util.concurrent.*;

/**
 * Representa um nó do grafo, possivelmente indicando se é um cruzamento, um cidadão ou um supermercado.
 */
class Node {
    int id;
    List<Node> adjacents = new ArrayList<>();
    NodeType nodeType;

    int x;
    int y;

    enum NodeType {
        INTERSECTION, CITIZEN, SUPERMARKET
    }

    public Node(int id, int x, int y) {
        this.id = id;
        this.nodeType = NodeType.INTERSECTION;
        this.x = x;
        this.y = y;
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
    public void setType(String type) {
        if(type == "SUPERMARKET"){
            this.nodeType = NodeType.SUPERMARKET;
        }
        if(type == "CITIZEN"){
            this.nodeType = NodeType.CITIZEN;
        }
        if(type == "INTERSECTION"){
            this.nodeType = NodeType.INTERSECTION;
        }
    }


}