package searchEngine;

import searchEngine.filters.Filter;
import searchEngine.tokenizers.Tokenizer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

public class InvertedIndexManager<K> {
    private final HashMap<String, HashSet<K>> invertedIndex;
    private final Vector<Filter> filters;
    private final Tokenizer tokenizer;
    private final HashSet<K> allKeys;

    public InvertedIndexManager(Vector<Filter> filters, Tokenizer tokenizer) {
        invertedIndex = new HashMap<>();
        this.tokenizer = tokenizer;
        this.filters = filters;
        this.allKeys = new HashSet<>();
    }

    public HashSet<K> findKeysByWord(String word) {
        return invertedIndex.get(word);
    }

    public void addData(HashMap<K, String> data) {
        allKeys.addAll(data.keySet());
        for (K key : data.keySet()) {
            String str = applyFilters(data.get(key));
            updateInvertedIndex(tokenizer.tokenize(str), key);
        }
    }

    private String applyFilters(String str) {
        for (Filter filter : filters) {
            str = filter.doFilter(str);
        }
        return str;
    }

    private void updateInvertedIndex(String[] words, K key) {
        for (String word : words) {
            if (word.isEmpty()) {
                continue;
            }
            if (invertedIndex.containsKey(word)) {
                invertedIndex.get(word).add(key);
            } else {
                HashSet<K> keys = new HashSet<>();
                keys.add(key);
                invertedIndex.put(word, keys);
            }
        }
    }

    public HashSet<K> getAllKeys() {
        return allKeys;
    }
}
