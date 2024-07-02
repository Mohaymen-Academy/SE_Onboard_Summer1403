package searchEngine;

import com.google.common.collect.ImmutableSet;
import searchEngine.filters.Filter;
import searchEngine.tokenizers.Tokenizer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

public class InvertedIndexManager<K> {
    private final HashMap<String, HashSet<K>> invertedIndex;
    private final Vector<Filter> filters;
    private final Tokenizer tokenizer;
    private final HashSet<K> allIDs;

    public InvertedIndexManager(Vector<Filter> filters, Tokenizer tokenizer) {
        invertedIndex = new HashMap<>();
        this.tokenizer = tokenizer;
        this.filters = filters;
        this.allIDs = new HashSet<>();
    }

    public HashSet<K> findIDsByWord(String word) {
        return invertedIndex.get(word);
    }

    public void addData(HashMap<K, String> data) {
        allIDs.addAll(data.keySet());
        for (K ID : data.keySet()) {
            String str = applyFilters(data.get(ID));
            updateInvertedIndex(tokenizer.tokenize(str), ID);
        }
    }

    private String applyFilters(String str) {
        for (Filter filter : filters) {
            str = filter.doFilter(str);
        }
        return str;
    }

    private void updateInvertedIndex(String[] words, K ID) {
        Arrays.stream(words).filter(w -> !w.isEmpty()).forEach(word ->
                invertedIndex.computeIfAbsent(word, k -> new HashSet<>()).add(ID));
    }

    public ImmutableSet<K> getAllIDs() {
        return ImmutableSet.copyOf(allIDs);
    }
}