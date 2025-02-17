import java.util.*;
import java.util.concurrent.*;

/**
 * Representa um caminho no grafo, usado pelo algoritmo para encontrar soluções.
 */
class Path {
    List<Node> nodes = new ArrayList<>();
    boolean finished = false; //indica se o caminho foi completado

    public Path(Node startNode) {
        nodes.add(startNode);
    }
    public Node getStartNode(){
        return nodes.get(0);
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

    @Override
    public String toString() {
        // Mostra os nós no caminho como uma string
        StringBuilder sb = new StringBuilder();
        for (Node node : nodes) {
            sb.append(node.id).append(" -> ");
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 4);  // Remove o último " -> "
        }
        return sb.toString();
    }

}