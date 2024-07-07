import search_engine.Document;
import search_engine.SearchEngine;
import search_engine.filters.LowerCaseFilter;
import search_engine.query_decoder.CommonQueryDecoder;
import search_engine.tokenizers.SpaceTokenizer;

import java.io.File;
import java.util.*;

public class Main {
    private final static String DOCUMENTS_DIRECTORY = "src/main/resources/docs";

    public static void main(String[] args) {
        List<File> files = FileUtilities.getFilesByDirPath(DOCUMENTS_DIRECTORY);
        List<Document> documents = getDocuments(files);


        SearchEngine searchEngine = SearchEngine.builder()
                .filters(List.of(new LowerCaseFilter()))
                .tokenizer(new SpaceTokenizer())
                .queryDecoder(new CommonQueryDecoder())
                .build();

        documents.forEach(searchEngine::addDocument);

        Scanner scanner = new Scanner(System.in);
        String query;
        while (!(query = scanner.nextLine()).equals("q")) {
            Set<String> result = searchEngine.search(query);
            printSet(result);
        }
    }

    private static List<Document> getDocuments(List<File> files) {
        List<Document> documents = new ArrayList<>();
        for (File file : files) {
            String content = FileUtilities.readFileContent(file);
            Document document = Document.builder()
                    .id(file.getName())
                    .content(content)
                    .build();
            documents.add(document);
        }
        return documents;
    }

    private static void printSet(Set<String> set) {
        if (set == null || set.isEmpty()) {
            System.out.println("nothing!");
            return;
        }
        set.forEach(System.out::println);
    }
}