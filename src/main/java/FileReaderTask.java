import java.io.*;
import java.util.*;

/**
 * Esta classe tem a finalidade de ler os ficheiros e extrair os dados deles retirados 
 */

class FileReaderTask {
    private final String filePath;

    public FileReaderTask(String filePath) {
        this.filePath = filePath;
    }

    public List<String> readFile() throws IOException {
        List<String> lines = new ArrayList<>();
        File file = new File("src/main/resources/file_test2/" + filePath);

        if (!file.exists()) throw new FileNotFoundException("Erro: o ficheiro "+ filePath + "não foi encontrado");

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        }

        System.out.println("Conteúdo do arquivo:");
        lines.forEach(System.out::println);

        return lines;
    }
}