import search_engine.Document;
import search_engine.InputQueryUtilities;
import search_engine.SearchEngine;
import search_engine.filters.LowerCaseFilter;
import search_engine.query_decoder.CommonQueryDecoder;
import search_engine.tokenizers.EdgeNgramTokenizer;

import java.util.List;

public class UsernameMain {
    public static void main(String[] args) {
        SearchEngine searchEngine = SearchEngine.builder()
                .filters(List.of(new LowerCaseFilter()))
                .tokenizer(new EdgeNgramTokenizer())
                .queryDecoder(new CommonQueryDecoder())
                .build();

        List<Document> documents = createDocuments();
        documents.forEach(searchEngine::addDocument);

        InputQueryUtilities.handleInputs(searchEngine);
    }

    private static List<Document> createDocuments() {
        List<String> usernames = List.of("omid", "ali", "alireza");
        return usernames.stream()
                .map(
                        username -> Document.builder()
                                .id(username)
                                .content(username)
                                .build()
                )
                .toList();
    }
}
