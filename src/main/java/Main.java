import java.io.*;
import java.security.spec.RSAOtherPrimeInfo;
import java.sql.Array;
import java.util.*;
import java.util.concurrent.Semaphore;

/**
 *  Esta classe tem a finalidade de executar o programa, chamando todas as outras classes.
 */
public class Main {


    public static void main(String[] args) {

            Scanner scanner = new Scanner(System.in);

            System.out.print("Número do ficheiro: ");
            String fileNumber = scanner.nextLine().trim();
            String filePath = "instances_" + fileNumber + ".txt";
            System.out.print("Largura do feixe: ");
            int beamWidth = Integer.parseInt(scanner.nextLine());
            System.out.print("Número de threads: ");
            int numThreads = Integer.parseInt(scanner.nextLine());
            System.out.print("Tempo de ms: ");
            int maxTime = Integer.parseInt(scanner.nextLine());
            System.out.println("Número de iterações: ");
            int searchSteps = scanner.nextInt();

            scanner.close();



            List<String> lines;    //Lista que armazena as linhas do ficheiro

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



            //numero de supermercados e de cidadãos
            int numSupermarkets = Integer.parseInt(lines.get(1).split(" ")[0]);
            int numCitizens = Integer.parseInt(lines.get(1).split(" ")[1]);

            //Coordenadas dos supermercados---------------------------------------------------------------------------------------------------
            System.out.println("------------------------nº de supermercados: " + numSupermarkets + "-------------------------------------------------");
            List<int[]> supermarketCoordinates = new ArrayList<>();

            for(int i = 2; i < numSupermarkets + 2; i++){
                String[] parts = lines.get(i).split(" ");   //chega as linhas de supermercado divide as a da esquerda é X e a outra Y
                int X = Integer.parseInt(parts[0]);               //e vai sempre fazendo isso ate  i < numSupermarkets+2
                int Y = Integer.parseInt(parts[1]);

                int[] auxSubermarketArray = {X , Y};
                supermarketCoordinates.add(auxSubermarketArray);

                System.out.println("Coordenadas do supermercado " + (i-1) + " -> (" + X + ", " + Y + ")");
            }

            //Coordenadas dos Cidadãos-----------------------------------------------------------------------------------------------------------
            System.out.println("------------------------nº de cidadãos: " + numCitizens + "------------------------------------------------------");
            List<int[]> citizenCoordinates = new ArrayList<>();

            for (int i = 2 + numSupermarkets; i < numCitizens + 2 + numSupermarkets; i++){
                String[] parts = lines.get(i).split(" ");
                int X = Integer.parseInt(parts[0]);
                int Y = Integer.parseInt(parts[1]);
                int[] auxCitizenArray = {X, Y};
                citizenCoordinates.add(auxCitizenArray);

                System.out.println("Coordenadas de cidadão " + (i-numSupermarkets - 1) + " -> (" + X + ", " + Y + ")");
            }


            // Extrai os primeiros valores do arquivo para configurar o grafo
            int M = Integer.parseInt(lines.get(0).split(" ")[0]);  //linhas verticais
            int N = Integer.parseInt(lines.get(0).split(" ")[1]);  //linhas horizontais
            int totalVertices = M * N + 2;    //nº total de vertices mais 2 pq é a contar com o nó inicial (casa) e final (supermercado)
            Graph graph = new Graph(totalVertices);
            graph.addNodeToGraph(M, N, supermarketCoordinates, citizenCoordinates);



            List<String> data = new ArrayList<>(lines);
            int totalTasks = numSupermarkets + numCitizens;  // Total de linhas úteis no ficheiro

            // Se o número de threads for maior do que o necessário, reduz
            numThreads = Math.min(numThreads, totalTasks);

            Semaphore semaphore = new Semaphore(numThreads);
            List<Thread> threads = new ArrayList<>();

        FindSolutionCitizensBS finder = new FindSolutionCitizensBS(beamWidth, numThreads, searchSteps, maxTime, graph);

        //graph.connectIntersections(M, N);  // adiciona as conexoes aos cruzamentos



        System.out.println("------------------------------threads----------------------------------------------------------");

        int chunkSize = (int)Math.ceil((double) totalTasks / numThreads);

        // Distribuição dos dados pelas threads
        int start = 2;  // Começamos a processar na linha 2 (ignorando cabeçalho M N e S C)

        while (start < data.size()) {
            int end = Math.min(start + chunkSize, data.size());  // Pelo menos 2 elementos por thread

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





            Solution solution = new Solution(graph);

            System.out.println("Caminhos encontrados para os cidadãos");
            for (Path path : solution.paths) { System.out.println("Cidadão " + path.getStartNode().id + " -> Caminho: " + path);
            }


        }
    }
