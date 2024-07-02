package searchEngine;

import searchEngine.decoders.Decoder;
import searchEngine.decoders.Query;

import java.util.HashSet;
import java.util.Optional;
import java.util.Vector;

public class QueryHandler<K> {
    private final InvertedIndexManager<K> invertedIndexManager;
    private final Decoder decoder;

    public QueryHandler(InvertedIndexManager<K> invertedIndexManager, Decoder decoder) {
        this.invertedIndexManager = invertedIndexManager;
        this.decoder = decoder;
    }

    private HashSet<K> removeForbidden(HashSet<K> base, HashSet<K> forbidden) {
        HashSet<K> result = new HashSet<>();
        for (K key : base) {
            if (!forbidden.contains(key)) {
                result.add(key);
            }
        }
        return result;
    }


    public HashSet<K> getQueryResult(String queryStr) {
        Query query = decoder.decode(queryStr.toLowerCase());
        HashSet<K> results = new HashSet<>();

        if (query.compulsories().isEmpty()) {
            if (query.optionals().isEmpty()) {
                if (!query.forbidden().isEmpty()) {
                    results = invertedIndexManager.getAllKeys();
                }
            } else {
                results = itemsUnion(query.optionals());
            }
        } else {
            results = intersectionCompulsories(query.compulsories());
            if (!query.optionals().isEmpty()) {
                HashSet<K> optionalKeys = itemsUnion(query.optionals());
                results.removeIf(s -> !optionalKeys.contains(s)); // results &= optionalKey
            }
        }

        HashSet<K> forbiddenKeys = itemsUnion(query.forbidden());
        return removeForbidden(results, forbiddenKeys);
    }


    private HashSet<K> intersectionCompulsories(Vector<String> compulsories) {
        Optional<String> baseWordOptional = findBaseWord(compulsories);

        if (baseWordOptional.isEmpty()) {
            return new HashSet<>();
        }

        String baseWord = baseWordOptional.get();

        HashSet<K> result = new HashSet<>(invertedIndexManager.findKeysByWord(baseWord));

        for (String compulsory : compulsories) {
            HashSet<K> foundKeys = invertedIndexManager.findKeysByWord(compulsory);
            result.removeIf(s -> !foundKeys.contains(s)); // result &= foundKey
            if (result.isEmpty()) {
                break;
            }
        }

        return result;
    }

    private Optional<String> findBaseWord(Vector<String> compulsories) {

        int minSize = Integer.MAX_VALUE;
        String baseWord = "";

        for (String compulsory : compulsories) {
            HashSet<K> foundKeys = invertedIndexManager.findKeysByWord(compulsory);
            
            if (foundKeys == null || foundKeys.isEmpty()) {
                return Optional.empty();
            }
            
            int size = foundKeys.size();
            if (size < minSize) {
                minSize = size;
                baseWord = compulsory;
            }
        }

        return Optional.of(baseWord);
    }

    private HashSet<K> itemsUnion(Vector<String> items) {
        HashSet<K> set = new HashSet<>();
        for (String item : items) {
            HashSet<K> foundKeys = invertedIndexManager.findKeysByWord(item);
            if (foundKeys != null) {
                set.addAll(foundKeys);
            }
        }
        return set;
    }
}
