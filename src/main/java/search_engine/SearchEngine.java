package search_engine;

import com.google.common.collect.ImmutableSet;
import org.apache.commons.collections4.CollectionUtils;
import search_engine.normalizers.Normalizer;
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
    private final List<Normalizer> normalizers;
    private final Tokenizer tokenizer;
    private final QueryDecoder decoder;

    private SearchEngine(List<Normalizer> normalizers, Tokenizer tokenizer, QueryDecoder decoder) {
        this.normalizers = normalizers == null ? new ArrayList<>() : normalizers;
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
        if (document.getContent() != null && !document.getContent().isEmpty())
            indexDocument(document.getId(), prepareWords(document.getContent()));
    }

    private List<String> prepareWords(String content) {
        List<String> words = tokenizer.tokenize(content);
        return words.stream()
                .map(this::applyNormalizers)
                .toList();
    }

    private String applyNormalizers(String content) {
        for (Normalizer normalizer : normalizers)
            content = normalizer.normalize(content);
        return content;
    }

    private void indexDocument(String id, List<String> words) {
        words.stream()
                .filter(word -> !word.isEmpty())
                .forEach(word -> invertedIndex.computeIfAbsent(word, k -> new HashSet<>()).add(id));
    }

    public ImmutableSet<String> search(String queryString) {
        Query query = decoder.decode(queryString);
        return ImmutableSet.copyOf(handleQuery(query));
    }

    private Set<String> handleQuery(Query query) {
        Set<String> results = new HashSet<>();

        if (CollectionUtils.isEmpty(query.includes())) {
            if (CollectionUtils.isEmpty(query.optionals())) {
                if (!CollectionUtils.isEmpty(query.excludes()))
                    results = docs.stream()
                            .map(Document::getId)
                            .collect(Collectors.toSet());
            } else results = unionSets(findMatchedDocs(query.optionals()));
        } else {
            results = intersectIncludes(query.includes());
            if (!CollectionUtils.isEmpty(query.optionals())) {
                Set<String> optionalIds = unionSets(findMatchedDocs(query.optionals()));
                results.removeIf(s -> !optionalIds.contains(s)); // results &= optionalIds
            }
        }

        Set<String> excludesIds = unionSets(findMatchedDocs(query.excludes()));
        return removeExcludes(results, excludesIds);
    }

    private Set<String> removeExcludes(Set<String> base, Set<String> excludes) {
        Set<String> result = new HashSet<>();
        base.stream()
                .parallel()
                .filter(id -> !excludes.contains(id))
                .forEach(result::add);
        return result;
    }

    private Set<String> intersectIncludes(List<String> includes) {
        Optional<Set<String>> baseWordOptional = findBaseSet(findMatchedDocs(includes));
        if (baseWordOptional.isEmpty())
            return new HashSet<>();

        Set<String> result = new HashSet<>(baseWordOptional.get());
        return intersectSets(result, findMatchedDocs(includes));
    }

    private List<Set<String>> findMatchedDocs(List<String> items) {
        return items.stream()
                .map(s -> invertedIndex.getOrDefault(s, new HashSet<>()))
                .toList();
    }

    private Set<String> intersectSets(Set<String> base, List<Set<String>> sets) {
        for (Set<String> set : sets) {
            base.removeIf(s -> !set.contains(s)); // result &= foundIds
            if (CollectionUtils.isEmpty(base))
                break;
        }
        return base;
    }

    private Set<String> unionSets(List<Set<String>> sets) {

        Set<String> result = new HashSet<>();

        sets.stream()
                .filter(Objects::nonNull)
                .forEach(result::addAll);

        return result;
    }

    private Optional<Set<String>> findBaseSet(List<Set<String>> sets) {
        int minSize = Integer.MAX_VALUE;
        Set<String> baseSet = new HashSet<>();

        for (Set<String> set : sets) {
            if (CollectionUtils.isEmpty(set)) return Optional.empty();

            int size = set.size();
            if (size < minSize) {
                minSize = size;
                baseSet = set;
            }
        }

        return Optional.of(baseSet);
    }

    public static class SearchEngineBuilder {
        private List<Normalizer> normalizers;
        private Tokenizer tokenizer;
        private QueryDecoder queryDecoder;

        SearchEngineBuilder() {
        }

        public SearchEngineBuilder normalizers(List<Normalizer> normalizers) {
            this.normalizers = normalizers;
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
            return new SearchEngine(this.normalizers, this.tokenizer, this.queryDecoder);
        }
    }
}
