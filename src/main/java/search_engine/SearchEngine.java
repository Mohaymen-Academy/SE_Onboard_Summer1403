package search_engine;

import com.google.common.collect.ImmutableSet;
import search_engine.filters.Filter;
import search_engine.queryDecoder.CommonQueryDecoder;
import search_engine.queryDecoder.Query;
import search_engine.queryDecoder.QueryDecoder;
import search_engine.tokenizers.SpaceTokenizer;
import search_engine.tokenizers.Tokenizer;

import java.util.*;
import java.util.stream.Collectors;

public class SearchEngine {
    private final Map<String, Set<String>> invertedIndex;
    private final List<Filter> filters;
    private final Tokenizer tokenizer;
    private final List<Document> docs;
    private final QueryDecoder decoder;

    public SearchEngine(List<Filter> filters, Tokenizer tokenizer, QueryDecoder decoder) {
        this.filters = filters == null ? new ArrayList<>() : filters;
        this.tokenizer = tokenizer == null ? new SpaceTokenizer() : tokenizer;
        this.decoder = decoder == null ? new CommonQueryDecoder() : decoder;
        docs = new ArrayList<>();
        invertedIndex = new HashMap<>();
    }

    public static SearchEngineBuilder builder() {
        return new SearchEngineBuilder();
    }

    public void addDocument(Document document) {
        if (document == null) return;
        docs.add(document);
        String content = applyFilters(document.getContent());
        indexDocument(tokenizer.tokenize(content), document.getId());
    }

    private String applyFilters(String content) {
        for (Filter filter : filters)
            content = filter.filter(content);
        return content;
    }

    public void indexDocument(List<String> words, String id) {
        words.stream().filter(w -> !w.isEmpty()).forEach(word -> invertedIndex.computeIfAbsent(word, k -> new HashSet<>()).add(id));
    }

    public ImmutableSet<String> search(String strQuery) {
        Query query = decoder.decode(strQuery);
        return ImmutableSet.copyOf(getQueryResult(query));
    }

    public Set<String> getQueryResult(Query query) {
        Set<String> results = new HashSet<>();

        if (query.includes().isEmpty()) {
            if (query.optionals().isEmpty()) {
                if (!query.excludes().isEmpty())
                    results = docs.stream().map(Document::getId).collect(Collectors.toSet());
            } else results = itemsUnion(query.optionals());
        } else {
                results = intersectionCompulsories(query.includes());
                if (!query.optionals().isEmpty()) {
                    Set<String> optionalIds = itemsUnion(query.optionals());
                    results.removeIf(s -> !optionalIds.contains(s)); // results &= optionalIds
                }
            }

        Set<String> excludesIds = itemsUnion(query.excludes());
        return removeExcludes(results, excludesIds);
    }

    private Set<String> removeExcludes(Set<String> base, Set<String> excludes) {
        Set<String> result = new HashSet<>();
        base.stream().parallel().filter(id -> !excludes.contains(id)).forEach(result::add);
        return new HashSet<>(result);
    }

    private Set<String> intersectionCompulsories(List<String> includes) {
        Optional<String> baseWordOptional = findBaseWord(includes);

        if (baseWordOptional.isEmpty()) return new HashSet<>();

        String baseWord = baseWordOptional.get();

        Set<String> result = new HashSet<>(invertedIndex.get(baseWord));

        for (String compulsory : includes) {
            Set<String> foundIds = invertedIndex.get(compulsory);
            result.removeIf(s -> !foundIds.contains(s)); // result &= foundIds
            if (result.isEmpty()) break;
        }

        return result;
    }

    private Optional<String> findBaseWord(List<String> includes) {
        int minSize = Integer.MAX_VALUE;
        String baseWord = "";

        for (String compulsory : includes) {
            Set<String> foundIds = invertedIndex.get(compulsory);

            if (foundIds == null || foundIds.isEmpty()) return Optional.empty();

            int size = foundIds.size();
            if (size < minSize) {
                minSize = size;
                baseWord = compulsory;
            }
        }

        return Optional.of(baseWord);
    }

    private Set<String> itemsUnion(List<String> items) {
        Set<String> set = new HashSet<>();
        for (String item : items) {
            Set<String> foundIds = invertedIndex.get(item);
            if (foundIds != null) {
                set.addAll(foundIds);
            }
        }
        return set;
    }

    public static class SearchEngineBuilder {
        private List<Filter> filters;
        private Tokenizer tokenizer;
        private QueryDecoder decoder;

        SearchEngineBuilder() {
        }

        public SearchEngineBuilder filters(List<Filter> filters) {
            this.filters = filters;
            return this;
        }

        public SearchEngineBuilder tokenizer(Tokenizer tokenizer) {
            this.tokenizer = tokenizer;
            return this;
        }

        public SearchEngineBuilder decoder(QueryDecoder decoder) {
            this.decoder = decoder;
            return this;
        }

        public SearchEngine build() {
            return new SearchEngine(this.filters, this.tokenizer, this.decoder);
        }
    }
}
