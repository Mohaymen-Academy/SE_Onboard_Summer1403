import org.apache.commons.collections4.CollectionUtils;
import search_engine.Document;
import search_engine.SearchEngine;
import search_engine.normalizers.LowerCaseNormalizer;
import search_engine.query_decoder.CommonQueryDecoder;
import search_engine.tokenizers.SpaceTokenizer;

import java.io.File;
import java.util.*;

public class FileMain {
    private final static String DOCUMENTS_DIRECTORY = "src/main/resources/docs";

    public static void main(String[] args) {
        List<File> files = FileUtilities.getFilesByDirPath(DOCUMENTS_DIRECTORY);
        List<Document> documents = getDocuments(files);


        SearchEngine searchEngine = SearchEngine.builder()
                .normalizers(List.of(new LowerCaseNormalizer()))
                .tokenizer(new SpaceTokenizer())
                .queryDecoder(new CommonQueryDecoder())
                .build();

        documents.forEach(searchEngine::addDocument);

        handleInputs(searchEngine);


    }

    private static List<Document> getDocuments(List<File> files) {
        return files.stream()
                .map(FileMain::fileToDocument)
                .toList();
    }

    private static Document fileToDocument(File file) {
        String content = FileUtilities.readFileContent(file);
        return Document.builder()
                .id(file.getName())
                .content(content)
                .build();
    }

    static void handleInputs(SearchEngine searchEngine) {
        Scanner scanner = new Scanner(System.in);
        String query;
        while (!(query = scanner.nextLine()).equals("q")) {
            Set<String> result = searchEngine.search(query);
            printCollection(result);
        }
        scanner.close();
    }

    static void printCollection(Collection<String> collection) {
        if (CollectionUtils.isEmpty(collection)) {
            System.out.println("nothing!");
            return;
        }
        collection.forEach(System.out::println);
    }
}