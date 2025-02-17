import java.io.*;
import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * esta classe tem a finalidade de Estruturar o grafo: cria os nós e arestas com base nos dados lidos.
 */
class Graph {
    private final int vertices;
    private final List<Set<Integer>> adjList;
    private final List<Node> citizens; // Lista de nós cidadãos

    public Graph(int vertices) {
        this.vertices = vertices;
        this.adjList = new ArrayList<>(vertices);
        this.citizens = new ArrayList<>();
        for (int i = 0; i < vertices; i++) {
            adjList.add(new HashSet<>());
            {
            }
        }
    }
    public synchronized void addEdge(int from, int to) {
        if (from != to && !adjList.get(from).contains(to)) {
        adjList.get(from).add(to);
        }
    }


    public void printGraph() {
        synchronized (System.out) {
            for (int i = 0; i < adjList.size(); i++) {
                System.out.println("Vértice " + i + " -> " + adjList.get(i));
            }
        }
    }

    public void connectIntersections (int M, int N) {
        for(int row = 0; row < M; row++ ){           //percorrer avenidas
            for(int colum = 0; colum < N; colum++) {       //percorrer ruas
                int node = row * N + colum;          //converte (avenida, rua) num indice do grafo

                if(colum < N-1){
                    addEdge(node, node + 1);
                }

                if(row < M-1){
                    addEdge(node, node + N);
                }
            }
        }
    }


    public List<Node> getCitizens() {
        return citizens;
    }

    public void addCitizen(Node citizen) {
        citizens.add(citizen);
    }
}
