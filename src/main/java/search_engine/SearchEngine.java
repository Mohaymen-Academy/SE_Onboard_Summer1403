package search_engine;

import com.google.common.collect.ImmutableSet;
import search_engine.decoders.CommonDecoder;
import search_engine.decoders.Decoder;
import search_engine.filters.Filter;
import search_engine.tokenizers.SpaceTokenizer;
import search_engine.tokenizers.Tokenizer;

import java.util.HashMap;
import java.util.Vector;

public class SearchEngine<K> {
    private final InvertedIndexManager<K> invertedIndexManager;
    private final QueryHandler<K> queryHandler;

    public SearchEngine(Vector<Filter> filters, Tokenizer tokenizer, Decoder decoder) {
        filters = filters == null ? new Vector<>() : filters;
        tokenizer = tokenizer == null ? new SpaceTokenizer() : tokenizer;
        decoder = decoder == null ? new CommonDecoder() : decoder;
        this.invertedIndexManager = new InvertedIndexManager<>(filters, tokenizer);
        this.queryHandler = new QueryHandler<>(invertedIndexManager, decoder);
    }

    public void addData(HashMap<K, String> data) {
        invertedIndexManager.addData(data);
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
        private Decoder decoder;

        SearchEngineBuilder() {}

        public SearchEngineBuilder<T> filters(Vector<Filter> filters) {
            this.filters = filters;
            return this;
        }

        public SearchEngineBuilder<T> tokenizer(Tokenizer tokenizer) {
            this.tokenizer = tokenizer;
            return this;
        }

        public SearchEngineBuilder<T> decoder(Decoder decoder) {
            this.decoder = decoder;
            return this;
        }

        public SearchEngine<T> build() {
            return new SearchEngine<>(this.filters, this.tokenizer, this.decoder);
        }
    }
}
