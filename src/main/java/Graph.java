import java.io.*;
import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * esta classe tem a finalidade de Estruturar o grafo: cria os nós e arestas com base nos dados lidos.
 */
class Graph {
    private final int vertices;
    private final List<Set<Integer>> adjList;
    private final List<Node> allGraphNodes; // Lista de nós cidadãos

    public Graph(int vertices) {
        this.vertices = vertices;
        this.adjList = new ArrayList<>(vertices);
        this.allGraphNodes = new ArrayList<Node>();
        for (int i = 0; i < vertices; i++) {
            adjList.add(new HashSet<>());
            {
            }
        }
    }

    /**
     * ler o tamanho do grafo que é pedido no ficheiro
     * gerar respetivas coordenadas com os seus X e Y
     * coocar dentro do allNodeToGraph
     * asseguir a colocar os nodes no grafo colocar os seus respetivos tipos
     */
    public void addNodeToGraph(int M, int N,List<int[]> supermarketCoordinates,List<int[]> citizenCoordinates){
        int nodeId = 1;
        for(int i = 1; i <= M; i++){ //verticais Y
            for(int j = 1; j <= N; j++){ //horizonais X
                Node node = new Node(nodeId, j, i);
                allGraphNodes.add(node);
                nodeId++;
            }
        }

        attributeNodeType(supermarketCoordinates, citizenCoordinates);
    }

    public void attributeNodeType(List<int[]> supermarketCoordinates,List<int[]> citizenCoordinates){
        for(int i = 0; i  < allGraphNodes.size(); i++){
            Node node = allGraphNodes.get(i);

            int x = node.x;
            int y = node.y;

            for(int k = 1; k <= supermarketCoordinates.size(); k++){

                if(supermarketCoordinates.get(k-1)[0] == x && supermarketCoordinates.get(k-1)[1] == y){
                    node.setType("SUPERMARKET");
                    System.out.println(node.nodeType);

                }
            }

            for(int k = 1; k <= citizenCoordinates.size(); k++){
                if(citizenCoordinates.get(k-1)[0] == x && citizenCoordinates.get(k-1)[1] == y){
                    node.setType("CITIZEN");
                    System.out.println(node.nodeType);


                }    //tava a dar out of bounds
            }

        }
    }


    public synchronized void addEdge(int from, int to) {
        if (from != to && !adjList.get(from).contains(to)) {
        adjList.get(from).add(to);
        }
    }


    public void printGraph() {
        String type = "INTERSECTION"; // Default
        synchronized (System.out) {
            for (int i = 0; i < this.allGraphNodes.size(); i++) {
                System.out.println("Vértice " + i + " (" + this.allGraphNodes.get(i).nodeType + " )" + adjList.get(i));
            }
        }
    }

    public void connectIntersections (int M, int N) {
        for(int row = 0; row < M; row++ ){           //percorrer avenidas
            for(int colum = 0; colum < N; colum++) {       //percorrer ruas
                int node = (row * N) + colum;          //converte (avenida, rua) num indice do grafo
                Node intersection = new Node(row, row, colum);

                if(colum < N-1){
                    addEdge(node, node + 1);
                    addEdge(node + 1, node); // Bidirecional
                }

                if(row < M-1){
                    addEdge(node, node + N);
                    addEdge(node + N, node);
                }
            }
        }
    }
    public List<Node> getCitizens() {
        return allGraphNodes;
    }

    public void addCitizen(Node citizen) {
//        citizens.get(citizen.id).setType(Node.NodeType.CITIZEN);

        //
    }

}
