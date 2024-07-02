import searchEngine.SearchEngine;

import java.io.*;
import java.util.*;

public class Main {
    private final static String filesPath = "src/resources/docs";

    public static void main(String[] args) {
        File[] files = FileHandler.getAllFiles(filesPath);
        HashMap<String, String> data = getData(files);


        SearchEngine<String> searchEngine = new SearchEngine<>(null, null, null);

        searchEngine.addData(data);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String query = scanner.nextLine();
            if (query.equals("q")) {
                break;
            }
            HashSet<String> result = searchEngine.search(query);
            printResult(result);
        }
    }

    private static HashMap<String, String> getData(File[] files) {
        HashMap<String, String> data = new HashMap<>();
        Arrays.stream(files).forEach(file -> {
            String fileContent;
            try {
                fileContent = FileHandler.readFileContent(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            data.put(file.getName(), fileContent);
        });
        return data;
    }

    private static void printResult(HashSet<String> results) {
        if (results.isEmpty()) {
            System.out.println("nothing!");
            return;
        }
        for (String str : results) {
            System.out.println(str);
        }
    }
}