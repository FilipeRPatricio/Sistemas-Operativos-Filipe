import java.io.*;
import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Implementa a concorrência, dividindo a execução do algoritmo em várias threads.
 */
class WorkerThread extends Thread {
    private final List<String> data;
    private final Semaphore semaphore;
    private final Graph graph;
    private final int M, N, totalVertices;

    public WorkerThread(List<String> data, Semaphore semaphore, Graph graph, int M, int N) {
        this.data = data;
        this.semaphore = semaphore;
        this.graph = graph;
        this.M = M;
        this.N = N;
        this.totalVertices = M * N +2;
    }

    @Override
    public void run() {
        try {
            semaphore.acquire();
            process();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            semaphore.release();
        }
    }

    private void process() {
        synchronized (System.out) {
            System.out.println(Thread.currentThread().getName() + " A processar: " + data);
        }

        if (data.isEmpty()  || data.size() < 2) {
            System.err.println("Erro: A thread " + Thread.currentThread().getName() + " recebeu poucos dados: " + data);
            return;
        }
        //obtem o nº de supermercados e cidadãos
        String[] firstLine = data.get(1).split(" ");
        if (firstLine.length < 2) {
            System.err.println("Erro: Linha inválida recebida pela thread " + Thread.currentThread().getName() + ": " + Arrays.toString(firstLine));
            return;
        }

        int numSupermarkets = Integer.parseInt(firstLine[0]);
        int numCitizens = Integer.parseInt(firstLine[1]);

        // Adiciona supermercados e cidadãos ao grafo
        for (int i = 2; i < data.size(); i++) {
            String[] coordinates = data.get(i).split(" ");
            int avenue = Integer.parseInt(coordinates[0]);   //M
            int street = Integer.parseInt(coordinates[1]);   //N
            int vertex = (avenue - 1) * N + (street - 1);    // indice do vertice

            if (i < 2 + numSupermarkets) {
                System.out.println("A ligar supermercado " + vertex + " ao vértice final " + (totalVertices -1));
                graph.addEdge(vertex, totalVertices -1);
            } else {
                System.out.println("A ligar cidadão " + vertex + " ao vértice inicial 0");
                graph.addEdge(0, vertex);
                graph.addCitizen(new Node(vertex));
            }
        }
    }
}