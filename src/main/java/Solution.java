import java.util.*;
import java.util.concurrent.*;

/**
 * 	Guarda os melhores caminhos encontrados e imprime os resultados.
 */
class Solution {
    List<Path> paths = new ArrayList<>();

    public Solution(Graph graph) {
        for (Node node : graph.getCitizens()) {  // a lista de cidadãos
            if(node.getType() == Node.NodeType.CITIZEN) {  // vê se é um cidadao
                paths.add(new Path(node));// Inicializa um caminho para cada cidadão
            }
        }
    }

    public int getScore(){
        return this.paths.size();  // Número de cidadãos que encontraram um caminho
    }

    public Solution(){};

    public Solution copy() {
        Solution newSolution = new Solution();
        for (Path path : paths) {
            Path newPath = new Path(path.nodes.get(0));
            newPath.nodes.addAll(path.nodes);
            newSolution.paths.add(newPath);
        }
        return newSolution;
    }

    public void addPath(Path path) {
        paths.add(path);
    }
}