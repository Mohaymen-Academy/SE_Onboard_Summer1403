package search_engine;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import search_engine.decoders.Decoder;
import search_engine.decoders.Query;

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
                    results = new HashSet<>(invertedIndexManager.getAllIds());
                }
            } else {
                results = itemsUnion(query.optionals());
            }
        } else {
            results = intersectionCompulsories(query.compulsories());
            if (!query.optionals().isEmpty()) {
                HashSet<K> optionalIds = itemsUnion(query.optionals());
                results.removeIf(s -> !optionalIds.contains(s)); // results &= optionalIds
            }
        }

        HashSet<K> forbiddenIds = itemsUnion(query.forbidden());
        return removeForbidden(results, forbiddenIds);
    }


    private ImmutableSet<K> removeForbidden(HashSet<K> base, HashSet<K> forbidden) {
        HashSet<K> result = new HashSet<>();
        base.stream().filter(id -> !forbidden.contains(id)).forEach(result::add);
        return ImmutableSet.copyOf(result);
    }



    private HashSet<K> intersectionCompulsories(ImmutableList<String> compulsories) {
        Optional<String> baseWordOptional = findBaseWord(compulsories);

        if (baseWordOptional.isEmpty()) {
            return new HashSet<>();
        }

        String baseWord = baseWordOptional.get();

        HashSet<K> result = new HashSet<>(invertedIndexManager.findIdsByWord(baseWord));

        for (String compulsory : compulsories) {
            HashSet<K> foundIds = invertedIndexManager.findIdsByWord(compulsory);
            result.removeIf(s -> !foundIds.contains(s)); // result &= foundIds
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
            HashSet<K> foundIds = invertedIndexManager.findIdsByWord(compulsory);
            
            if (foundIds == null || foundIds.isEmpty()) {
                return Optional.empty();
            }
            
            int size = foundIds.size();
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
            HashSet<K> foundIds = invertedIndexManager.findIdsByWord(item);
            if (foundIds != null) {
                set.addAll(foundIds);
            }
        }
        return set;
    }
}
