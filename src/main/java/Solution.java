import java.util.*;
import java.util.concurrent.*;

/**
 * 	Guarda os melhores caminhos encontrados e imprime os resultados.
 */
class Solution {
    List<Path> paths = new ArrayList<>();

    public Solution(Graph graph) {
        for (Node citizen : graph.getCitizens()) { // Supondo que há um método getCitizens()
            paths.add(new Path(citizen)); // Inicializa um caminho para cada cidadão
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