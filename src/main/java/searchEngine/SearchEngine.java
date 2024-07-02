package searchEngine;

import com.google.common.collect.ImmutableSet;
import searchEngine.decoders.CommonDecoder;
import searchEngine.decoders.Decoder;
import searchEngine.filters.Filter;
import searchEngine.tokenizers.SpaceTokenizer;
import searchEngine.tokenizers.Tokenizer;

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
}
