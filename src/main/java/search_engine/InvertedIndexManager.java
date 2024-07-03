package search_engine;

import com.google.common.collect.ImmutableSet;
import search_engine.filters.Filter;
import search_engine.tokenizers.Tokenizer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

public class InvertedIndexManager<K> {
    private final HashMap<String, HashSet<K>> invertedIndex;
    private final Vector<Filter> filters;
    private final Tokenizer tokenizer;
    private final HashSet<K> allIds;

    public InvertedIndexManager(Vector<Filter> filters, Tokenizer tokenizer) {
        invertedIndex = new HashMap<>();
        this.tokenizer = tokenizer;
        this.filters = filters;
        this.allIds = new HashSet<>();
    }

    public HashSet<K> findIdsByWord(String word) {
        return invertedIndex.get(word);
    }

    public void addData(HashMap<K, String> data) {
        allIds.addAll(data.keySet());
        for (K id : data.keySet()) {
            String str = applyFilters(data.get(id));
            updateInvertedIndex(tokenizer.tokenize(str), id);
        }
    }

    private String applyFilters(String str) {
        for (Filter filter : filters) {
            str = filter.doFilter(str);
        }
        return str;
    }

    private void updateInvertedIndex(String[] words, K id) {
        Arrays.stream(words).filter(w -> !w.isEmpty()).forEach(word ->
                invertedIndex.computeIfAbsent(word, k -> new HashSet<>()).add(id));
    }

    public ImmutableSet<K> getAllIds() {
        return ImmutableSet.copyOf(allIds);
    }
}