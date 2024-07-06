package search_engine;

import com.google.common.collect.ImmutableSet;
import search_engine.filters.Filter;
import search_engine.queryDecoders.CommonQueryDecoder;
import search_engine.queryDecoders.Query;
import search_engine.queryDecoders.QueryDecoder;
import search_engine.tokenizers.SpaceTokenizer;
import search_engine.tokenizers.Tokenizer;

import java.util.*;

public class SearchEngine<K> {
    private final Map<String, Set<String>> invertedIndex;
    private final List<Filter> filters;
    private final Tokenizer tokenizer;
    private final List<Document> docs;
    private final QueryDecoder decoder;
    //private final DocumentUtility documentUtility;

    public SearchEngine(Vector<Filter> filters, Tokenizer tokenizer, QueryDecoder decoder) {
        this.filters = filters == null ? new Vector<>() : filters;
        this.tokenizer = tokenizer == null ? new SpaceTokenizer() : tokenizer;
        this.decoder = decoder == null ? new CommonQueryDecoder() : decoder;
        docs = new ArrayList<>();
        invertedIndex = new HashMap<>();
        //documentUtility = new DocumentUtility<>(filters, tokenizer);
    }

    public static <T> SearchEngineBuilder<T> builder() {
        return new SearchEngineBuilder<>();
    }

    public void addDocument(Document document) {
        if (document == null)
            return;
        docs.add(document);
        String content = applyFilters(document.getContent());
        indexDocument(tokenizer.tokenize(content), document.getId());
    }

    private String applyFilters(String content) {
        for (Filter filter : filters) {
            content = filter.filter(content);
        }
        return content;
    }

    public void indexDocument(String[] words, String id) {
        Arrays.stream(words).filter(w -> !w.isEmpty()).forEach(word ->
                invertedIndex.computeIfAbsent(word, k -> new HashSet<>()).add(id));
    }





    public ImmutableSet<K> search(String query) {
        Query queryIn = decoder.decode(query);

        return null;
    }




    public static class SearchEngineBuilder<T> {
        private Vector<Filter> filters;
        private Tokenizer tokenizer;
        private QueryDecoder decoder;

        SearchEngineBuilder() {
        }

        public SearchEngineBuilder<T> filters(Vector<Filter> filters) {
            this.filters = filters;
            return this;
        }

        public SearchEngineBuilder<T> tokenizer(Tokenizer tokenizer) {
            this.tokenizer = tokenizer;
            return this;
        }

        public SearchEngineBuilder<T> decoder(QueryDecoder decoder) {
            this.decoder = decoder;
            return this;
        }

        public SearchEngine<T> build() {
            return new SearchEngine<>(this.filters, this.tokenizer, this.decoder);
        }
    }
}
