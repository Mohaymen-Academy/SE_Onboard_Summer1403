import com.google.common.collect.ImmutableSet;
import search_engine.SearchEngine;
import search_engine.decoders.CommonDecoder;
import search_engine.tokenizers.SpaceTokenizer;

import java.io.*;
import java.util.*;

public class Main {
    private final static String DIR_PATH = "src/main/resources/docs";

    public static void main(String[] args) {
        File[] files = FileUtilities.getFilesByDirPath(DIR_PATH);
        HashMap<String, String> data = getData(files);


        SearchEngine<String> searchEngine = SearchEngine.<String>builder()
                .tokenizer(new SpaceTokenizer())
                .decoder(new CommonDecoder()).build();

        searchEngine.addData(data);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String query = scanner.nextLine();
            if (query.equals("q"))
                break;
            ImmutableSet<String> result = searchEngine.search(query);
            printResult(result);
        }
    }

    private static HashMap<String, String> getData(File[] files) {
        HashMap<String, String> data = new HashMap<>();
        for (File file : files) {
            String content = FileUtilities.readFileContent(file);
            String normalizedContent = normalizeStr(content);
            data.put(file.getName(), normalizedContent);
        }
        return data;
    }

    private static String normalizeStr(String content) {
        return content.toLowerCase().replaceAll("\n", " ");
    }

    private static void printResult(ImmutableSet<String> results) {
        if (results.isEmpty()) {
            System.out.println("nothing!");
            return;
        }
        for (String str : results) {
            System.out.println(str);
        }
    }
}