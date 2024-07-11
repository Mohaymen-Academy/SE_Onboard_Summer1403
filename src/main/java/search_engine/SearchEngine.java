package search_engine;

import com.google.common.collect.ImmutableSet;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import search_engine.enums.Status;
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
        if (!StringUtils.isEmpty(document.getContent())) {
            indexDocument(document.getId(), prepareWords(document.getContent()));
        }
    }

    private List<String> prepareWords(String content) {
        List<String> words = tokenizer.tokenize(content);
        return words.stream().map(this::applyNormalizers).toList();
    }

    private String applyNormalizers(String content) {
        for (Normalizer normalizer : normalizers)
            content = normalizer.normalize(content);
        return content;
    }

    private void indexDocument(String id, List<String> words) {
        words.stream().filter(word -> !word.isEmpty()).forEach(word -> invertedIndex.computeIfAbsent(word, k -> new HashSet<>()).add(id));
    }

    public ImmutableSet<String> search(String queryString) {
        Query query = decoder.decode(queryString);
        return ImmutableSet.copyOf(handleQuery(query));
    }

    private Set<String> handleQuery(Query query) {
        Status status = query.getStatus();
        Set<String> results = new HashSet<>();
        List<Set<String>> includesSet = convertTokensToSetOfDocsId(query.includes());
        List<Set<String>> optionalsSet = convertTokensToSetOfDocsId(query.optionals());
        List<Set<String>> excludesSet = convertTokensToSetOfDocsId(query.excludes());

        switch (status) {
            case JUST_EXCLUDES -> results = docs.stream().map(Document::getId).collect(Collectors.toSet());

            case JUST_OPTIONAL -> results = unionSets(optionalsSet);

            case JUST_INCLUDES -> {
                Optional<Set<String>> includesBaseSet = findBaseSet(includesSet);

                if (includesBaseSet.isEmpty()) return new HashSet<>();
                results = intersectSets(includesBaseSet.get(), includesSet);
            }

            case HAVE_OPTIONALS -> {
                Optional<Set<String>> includesBaseSet = findBaseSet(includesSet);

                if (includesBaseSet.isEmpty()) return new HashSet<>();
                results = intersectSets(includesBaseSet.get(), includesSet);
                results.removeIf(s -> !unionSets(optionalsSet).contains(s)); // results &= optionalIds
            }

            case EMPTY -> {
                return new HashSet<>();
            }
        }

        return removeSets(results, unionSets(excludesSet));
    }


    private List<Set<String>> convertTokensToSetOfDocsId(List<String> items) {
        return items == null ?
                new ArrayList<>() :
                items.stream()
                        .map(this::getDocsIdByWord)
                        .toList();
    }

    private Set<String> getDocsIdByWord(String token) {
        return invertedIndex.getOrDefault(token, new HashSet<>());
    }

    private Set<String> removeSets(Set<String> base, Set<String> excludes) {
        Set<String> result = new HashSet<>();
        base.stream().parallel().filter(id -> !excludes.contains(id)).forEach(result::add);
        return result;
    }


    private Set<String> intersectSets(Set<String> base, List<Set<String>> sets) {
        for (Set<String> set : sets) {
            base.removeIf(s -> !set.contains(s)); // result &= foundIds
            if (CollectionUtils.isEmpty(base)) break;
        }
        return base;
    }


    private Set<String> unionSets(List<Set<String>> sets) {
        return sets.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }


    private Optional<Set<String>> findBaseSet(List<Set<String>> sets) {
        return sets.stream().min(Comparator.comparingInt(Set::size));
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
