import searchEngine.SearchEngine;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class Main {
    private static String filesPath = "src/resources/docs";

    public static void main(String[] args) throws IOException {
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

    private static HashMap<String, String> getData(File[] files) throws IOException {
        HashMap<String, String> data = new HashMap<>();
        for (File file : files) {
            String fileContent = FileHandler.readFileContent(file);
            data.put(file.getName(), fileContent);
        }
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