import java.util.*;
import java.util.concurrent.*;

/**
 * Esta classe contém o algoritmo Beam Search para encontrar os caminhos ideais.
 */
public class FindSolutionCitizensBS {
    private final int beamSize;
    private final int numThreads;
    private final int searchSteps;
    private final long maxTime;
    private final Graph graph;
    private final Semaphore semaphore = new Semaphore(1);
    private volatile Solution bestSolution;

    public FindSolutionCitizensBS(int beamSize, int numThreads, int searchSteps, long maxTime, Graph graph) {
        this.beamSize = beamSize;
        this.numThreads = numThreads;
        this.searchSteps = searchSteps;
        this.maxTime = maxTime;
        this.graph = graph;
        this.bestSolution = new Solution();
    }

    public Solution findSolution() throws InterruptedException {
        long startTime = System.currentTimeMillis();
        List<Solution> beam = Collections.synchronizedList(new ArrayList<>());
        beam.add(new Solution(graph));
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        while (System.currentTimeMillis() - startTime < maxTime && !beam.isEmpty()) {
            List<Solution> nextBeam = Collections.synchronizedList(new ArrayList<>());
            List<Future<?>> futures = new ArrayList<>();
            int chunkSize = (int) Math.ceil((double) beam.size() / numThreads);

            for (int i = 0; i < numThreads; i++) {
                int start = i * chunkSize;
                int end = Math.min(start + chunkSize, beam.size());
                if (start >= end) break;

                List<Solution> subList = beam.subList(start, end);
                futures.add(executor.submit(() -> processBeamChunk(subList, nextBeam)));
            }

            for (Future<?> future : futures) {
                try {
                    future.get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

            if (nextBeam.isEmpty()) break;

            // Agora selecionamos apenas as melhores soluções com um critério mais inteligente
            nextBeam.sort(Comparator.comparingInt(s -> -s.getScore())); // Ordena pelo número de cidadãos atendidos

            if (nextBeam.size() > beamSize) {
                beam = new ArrayList<>(nextBeam.subList(0, beamSize)); // Mantemos apenas as melhores soluções
            } else {
                beam = nextBeam;
            }
        }

        executor.shutdown();
        return bestSolution;
    }

    private void processBeamChunk(List<Solution> subBeam, List<Solution> nextBeam) {
        for (Solution sol : subBeam) {
            for (int i = 0; i < searchSteps; i++) {
                expand(sol, nextBeam);
            }
        }
    }

    private void expand(Solution sol, List<Solution> nextBeam) {
        for (Path path : sol.paths) {
            if (path.finished) continue;
            Node lastNode = path.lastNode();

            for (Node adj : lastNode.getAdjacents()) {
                if (path.contains(adj)) continue;
                Solution newSolution = sol.copy();
                int pathIndex = sol.paths.lastIndexOf(path);
                if(pathIndex >= 0){
                newSolution.paths.get(sol.paths.indexOf(path)).addNode(adj); }

                try {
                    semaphore.acquire();
                    if (isBetterSolution(newSolution)) {
                        bestSolution = newSolution;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }

                nextBeam.add(newSolution);
            }
        }
    }

    private boolean isBetterSolution(Solution newSolution) {
        return newSolution.paths.size() > bestSolution.paths.size();
    }
}
