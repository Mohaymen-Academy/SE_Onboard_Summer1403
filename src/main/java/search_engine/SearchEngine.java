package search_engine;

import com.google.common.collect.ImmutableSet;
import search_engine.filters.Filter;
import search_engine.query_decoder.CommonQueryDecoder;
import search_engine.query_decoder.Query;
import search_engine.query_decoder.QueryDecoder;
import search_engine.tokenizers.SpaceTokenizer;
import search_engine.tokenizers.Tokenizer;

import java.util.*;
import java.util.stream.Collectors;

public class SearchEngine {
    private final Map<String, Set<String>> invertedIndex;
    private final List<Document> docs;
    private final List<Filter> filters;
    private final Tokenizer tokenizer;
    private final QueryDecoder decoder;

    private SearchEngine(List<Filter> filters, Tokenizer tokenizer, QueryDecoder decoder) {
        this.filters = filters == null ? new ArrayList<>() : filters;
        this.tokenizer = tokenizer == null ? new SpaceTokenizer() : tokenizer;
        this.decoder = decoder == null ? new CommonQueryDecoder() : decoder;
        docs = new ArrayList<>();
        invertedIndex = new HashMap<>();
    }

    public void addDocument(Document document) {
        if (document == null) return;
        docs.add(document);
        indexDocument(document.getId(), prepareWords(document.getContent()));
    }

    private List<String> prepareWords(String content) {
        List<String> words = tokenizer.tokenize(content);
        return words.stream().map(this::applyFilters).toList();
    }

    private String applyFilters(String content) {
        for (Filter filter : filters)
            content = filter.filter(content);
        return content;
    }

    private void indexDocument(String id, List<String> words) {
        words.stream()
                .filter(word -> !word.isEmpty())
                .forEach(word -> invertedIndex.computeIfAbsent
                        (word, k -> new HashSet<>()).add(id));
    }

    public ImmutableSet<String> search(String queryString) {
        Query query = decoder.decode(queryString);
        return ImmutableSet.copyOf(handleQuery(query));
    }

    private Set<String> handleQuery(Query query) {
        Set<String> results = new HashSet<>();

        if (query.includes() == null || query.includes().isEmpty()) {
            if (query.optionals().isEmpty()) {
                if (!query.excludes().isEmpty())

                    results = docs.stream()
                            .map(Document::getId)
                            .collect(Collectors.toSet());
            } else results = setsUnion(query.optionals());
        } else {
                results = intersectIncludes(query.includes());
                if (!query.optionals().isEmpty()) {
                    Set<String> optionalIds = setsUnion(query.optionals());
                    results.removeIf(s -> !optionalIds.contains(s)); // results &= optionalIds
                }
            }

        Set<String> excludesIds = setsUnion(query.excludes());
        return removeExcludes(results, excludesIds);
    }

    private Set<String> removeExcludes(Set<String> base, Set<String> excludes) {
        Set<String> result = new HashSet<>();
        base.stream().parallel().filter(id -> !excludes.contains(id)).forEach(result::add);
        return result;
    }

    // private


    private Set<String> intersectIncludes(List<String> includes) {
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

        for (String include : includes) {
            Set<String> foundIds = invertedIndex.get(include);

            if (foundIds == null || foundIds.isEmpty()) return Optional.empty();

            int size = foundIds.size();
            if (size < minSize) {
                minSize = size;
                baseWord = include;
            }
        }

        return Optional.of(baseWord);
    }

    private List<Set<String>> findMatchedDocs(List<String> items) {
        return items.stream()
                .map(invertedIndex::get)
                .toList();
    }


    private Set<String> setsUnion(List<Set<String>> sets) {

        Set<String> result = new HashSet<>();

        sets.stream()
                .filter(Objects::nonNull)
                .forEach(result::addAll);

        return result;
    }

    public static SearchEngineBuilder builder() {
        return new SearchEngineBuilder();
    }

    public static class SearchEngineBuilder {
        private List<Filter> filters;
        private Tokenizer tokenizer;
        private QueryDecoder queryDecoder;

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

        public SearchEngineBuilder queryDecoder(QueryDecoder decoder) {
            this.queryDecoder = decoder;
            return this;
        }

        public SearchEngine build() {
            return new SearchEngine(this.filters, this.tokenizer, this.queryDecoder);
        }
    }
}
