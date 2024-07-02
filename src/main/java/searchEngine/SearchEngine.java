package searchEngine;

import com.google.common.collect.ImmutableSet;
import searchEngine.decoders.CommonDecoder;
import searchEngine.decoders.Decoder;
import searchEngine.filters.Filter;
import searchEngine.tokenizers.SpaceTokenizer;
import searchEngine.tokenizers.Tokenizer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

public class SearchEngine<K> {
    private final InvertedIndexManager<K> invertedIndexManager;
    private final QueryHandler<K> queryHandler;

    public SearchEngine(Vector<Filter> filters, Tokenizer tokenizer, Decoder decoder) {
        Vector<Filter> filters1 = filters == null ? new Vector<>() : filters;
        Tokenizer tokenizer1 = tokenizer == null ? new SpaceTokenizer() : tokenizer;
        Decoder decoder1 = decoder == null ? new CommonDecoder() : decoder;
        this.invertedIndexManager = new InvertedIndexManager<>(filters1, tokenizer1);
        this.queryHandler = new QueryHandler<>(invertedIndexManager, decoder1);
    }

    public void addData(HashMap<K, String> data) {
        invertedIndexManager.addData(data);
    }

    public ImmutableSet<K> search(String query) {
        return queryHandler.getQueryResult(query);
    }
}
