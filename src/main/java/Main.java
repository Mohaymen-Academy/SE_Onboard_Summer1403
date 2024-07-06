import com.google.common.collect.ImmutableSet;
import search_engine.Document;
import search_engine.SearchEngine;
import search_engine.queryDecoders.CommonQueryDecoder;
import search_engine.tokenizers.SpaceTokenizer;

import java.io.File;
import java.util.*;

public class Main {
    private final static String DIR_PATH = "src/main/resources/docs";

    public static void main(String[] args) {
        File[] files = FileUtilities.getFilesByDirPath(DIR_PATH);
        List<Document> documents = getDocuments(files);


        SearchEngine<String> searchEngine = SearchEngine.<String>builder()
                .tokenizer(new SpaceTokenizer())
                .decoder(new CommonQueryDecoder())
                .build();

        documents.forEach(searchEngine::addDocument);

        Scanner scanner = new Scanner(System.in);
        String query;
        while (!(query = scanner.nextLine()).equals("q")) {
            Set<String> result = searchEngine.search(query);
            printResult(result);
        }
    }

    private static List<Document> getDocuments(File[] files) {
        List<Document> documents = new ArrayList<>();
        for (File file : files) {
            String content = FileUtilities.readFileContent(file);
            String normalizedContent = normalizeStr(content);
            Document document = Document.builder()
                    .id(file.getName())
                    .content(normalizedContent)
                    .build();
            documents.add(document);
        }
        return documents;
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