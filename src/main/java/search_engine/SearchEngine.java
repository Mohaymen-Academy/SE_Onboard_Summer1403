package search_engine;

import com.google.common.collect.ImmutableSet;
import search_engine.queryDecoders.CommonQueryDecoder;
import search_engine.queryDecoders.QueryDecoder;
import search_engine.filters.Filter;
import search_engine.tokenizers.SpaceTokenizer;
import search_engine.tokenizers.Tokenizer;

import java.util.*;

public class SearchEngine<K> {
    private final Map<String, List<MatchingDoc>> invertedIndex;
    private final List<Filter> filters;
    private final Tokenizer tokenizer;
    private final List<Document> docs;
    private final QueryDecoder decoder;

    public SearchEngine(Vector<Filter> filters, Tokenizer tokenizer, QueryDecoder decoder) {
        this.filters = filters == null ? new Vector<>() : filters;
        this.tokenizer = tokenizer == null ? new SpaceTokenizer() : tokenizer;
        this.decoder = decoder == null ? new CommonQueryDecoder() : decoder;
        docs = new ArrayList<>();
        invertedIndex = new HashMap<>();
    }

    public void addDocument(Document document) {
        if (document != null)
            docs.add(document);
    }

    public ImmutableSet<K> search(String query) {
        return queryHandler.getQueryResult(query);
    }

    public static <T> SearchEngineBuilder<T> builder() {
        return new SearchEngineBuilder<>();
    }

    public static class SearchEngineBuilder<T> {
        private Vector<Filter> filters;
        private Tokenizer tokenizer;
        private QueryDecoder decoder;

        SearchEngineBuilder() {}

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
