package searchEngine;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import searchEngine.decoders.Decoder;
import searchEngine.decoders.Query;

import java.util.HashSet;
import java.util.Optional;

public class QueryHandler<K> {
    private final InvertedIndexManager<K> invertedIndexManager;
    private final Decoder decoder;

    public QueryHandler(InvertedIndexManager<K> invertedIndexManager, Decoder decoder) {
        this.invertedIndexManager = invertedIndexManager;
        this.decoder = decoder;
    }


    public ImmutableSet<K> getQueryResult(String queryStr) {
        Query query = decoder.decode(queryStr.toLowerCase());
        HashSet<K> results = new HashSet<>();

        if (query.compulsories().isEmpty()) {
            if (query.optionals().isEmpty()) {
                if (!query.forbidden().isEmpty()) {
                    results = new HashSet<>(invertedIndexManager.getAllIDs());
                }
            } else {
                results = itemsUnion(query.optionals());
            }
        } else {
            results = intersectionCompulsories(query.compulsories());
            if (!query.optionals().isEmpty()) {
                HashSet<K> optionalIDs = itemsUnion(query.optionals());
                results.removeIf(s -> !optionalIDs.contains(s)); // results &= optionalIDs
            }
        }

        HashSet<K> forbiddenIDs = itemsUnion(query.forbidden());
        return removeForbidden(results, forbiddenIDs);
    }


    private ImmutableSet<K> removeForbidden(HashSet<K> base, HashSet<K> forbidden) {
        HashSet<K> result = new HashSet<>();
        base.stream().filter(ID -> !forbidden.contains(ID)).forEach(result::add);
        return ImmutableSet.copyOf(result);
    }



    private HashSet<K> intersectionCompulsories(ImmutableList<String> compulsories) {
        Optional<String> baseWordOptional = findBaseWord(compulsories);

        if (baseWordOptional.isEmpty()) {
            return new HashSet<>();
        }

        String baseWord = baseWordOptional.get();

        HashSet<K> result = new HashSet<>(invertedIndexManager.findIDsByWord(baseWord));

        for (String compulsory : compulsories) {
            HashSet<K> foundIDs = invertedIndexManager.findIDsByWord(compulsory);
            result.removeIf(s -> !foundIDs.contains(s)); // result &= foundIDs
            if (result.isEmpty()) {
                break;
            }
        }

        return result;
    }

    private Optional<String> findBaseWord(ImmutableList<String> compulsories) {
        int minSize = Integer.MAX_VALUE;
        String baseWord = "";

        for (String compulsory : compulsories) {
            HashSet<K> foundIDs = invertedIndexManager.findIDsByWord(compulsory);
            
            if (foundIDs == null || foundIDs.isEmpty()) {
                return Optional.empty();
            }
            
            int size = foundIDs.size();
            if (size < minSize) {
                minSize = size;
                baseWord = compulsory;
            }
        }

        return Optional.of(baseWord);
    }

    private HashSet<K> itemsUnion(ImmutableList<String> items) {
        HashSet<K> set = new HashSet<>();
        for (String item : items) {
            HashSet<K> foundIDs = invertedIndexManager.findIDsByWord(item);
            if (foundIDs != null) {
                set.addAll(foundIDs);
            }
        }
        return set;
    }
}
