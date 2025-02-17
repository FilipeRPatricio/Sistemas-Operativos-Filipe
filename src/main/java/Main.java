import java.io.*;
import java.util.*;
import java.util.concurrent.Semaphore;

/**
 *  Esta classe tem a finalidade de executar o programa, chamando todas as outras classes.
 */
public class Main {


    public static void main(String[] args) {

            Scanner scanner = new Scanner(System.in);

            System.out.print("Nome do ficheiro: ");
            String filePath = scanner.nextLine();
            System.out.print("Largura do feixe: ");
            int beamWidth = Integer.parseInt(scanner.nextLine());
            System.out.print("Número de threads: ");
            int numThreads = Integer.parseInt(scanner.nextLine());
            System.out.print("Tempo de ms: ");
            int maxTime = Integer.parseInt(scanner.nextLine());
            System.out.println("Número de iterações: ");
            int searchSteps = scanner.nextInt();

            scanner.close();



            List<String> lines;    //Lista que armazzena as linhas do ficheiro

            try {
                System.out.println("A iniciar leitura do ficheiro ");
                FileReaderTask fileReader = new FileReaderTask(filePath);
                lines = fileReader.readFile(); // lê o ficheiro e armazena as linhas na "lines"
                lines.forEach(System.out::println);
            } catch (IOException e) {
                System.out.println("Erro ao processar ficheiro " + e.getMessage());
                return;
            }

            if (lines.isEmpty()) {
                System.out.println("Erro: O ficheiro está vazio ou não foi lido corretamente ");
                return;
            }

            // Extrai os primeiros valores do arquivo para configurar o grafo
            int M = Integer.parseInt(lines.get(0).split(" ")[0]);  //linhas verticais
            int N = Integer.parseInt(lines.get(0).split(" ")[1]);  //linhas horizontais
            int totalVertices = M * N + 2;    //nº total de vertices mais 2 pq é a contar com o nó inicial (casa) e final (supermercado)
            Graph graph = new Graph(totalVertices);


            int numSupermarkets = Integer.parseInt(lines.get(1).split(" ")[0]);
            int numCitizens = Integer.parseInt(lines.get(1).split(" ")[1]);

            List<String> data = new ArrayList<>(lines);
            int totalTasks = numSupermarkets + numCitizens;  // Total de linhas úteis no ficheiro

            // Se o número de threads for maior do que o necessário, reduz
            numThreads = Math.min(numThreads, totalTasks);

            Semaphore semaphore = new Semaphore(numThreads);
            List<Thread> threads = new ArrayList<>();

        FindSolutionCitizensBS finder = new FindSolutionCitizensBS(beamWidth, numThreads, searchSteps, maxTime, graph);

        graph.connectIntersections(M, N);  // adiciona as conexoes aos cruzamentos

        // Distribuição dos dados pelas threads
        int start = 2;  // Começamos a processar na linha 2 (ignorando cabeçalho M N e S C)

        while (start < data.size()) {
            int end = Math.min(start + 2, data.size());  // Pelo menos 2 elementos por thread

            List<String> subData = data.subList(start, end);

            if (!subData.isEmpty()) { // Garante que a thread tem dados suficientes
                Thread thread = new WorkerThread(subData, semaphore, graph, M, N);
                threads.add(thread);
                thread.start();
            } else {
                System.err.println("Thread ignorada (poucos dados): " + subData);
            }

            start = end;
        }

            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            System.out.println("Grafo Criado:");
            graph.printGraph();
            System.out.println("Processamento concluído!");

/**
            Solution solution = new Solution(graph);

            System.out.println("Caminhos encontrados para os cidadãos");
            for (Path path : solution.paths) { System.out.println("Cidadão " + path.getStartNode().id + " -> Caminho: " + path);
            }

**/

        }
    }
