import search_engine.Document;
import search_engine.InputQueryUtilities;
import search_engine.SearchEngine;
import search_engine.filters.LowerCaseFilter;
import search_engine.query_decoder.CommonQueryDecoder;
import search_engine.tokenizers.SpaceTokenizer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileMain {
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

        InputQueryUtilities.handleInputs(searchEngine);
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
}