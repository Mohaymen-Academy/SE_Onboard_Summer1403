package search_engine;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import search_engine.filters.NumberFilter;
import search_engine.query_decoder.CommonQueryDecoder;
import search_engine.tokenizers.SpaceTokenizer;

import java.util.ArrayList;
import java.util.List;

class SearchEngineBuilderTest {

    SearchEngine underTest = SearchEngine.builder()
            .tokenizer(new SpaceTokenizer())
            .queryDecoder(new CommonQueryDecoder()).build();

    @Test
    public void buildWithoutArgument_noException() {
        SearchEngine searchEngine;
        Assertions.assertDoesNotThrow(() -> SearchEngine.builder().build(),
                "build with no argument shouldn't throw exception");

    }

    @Test
    public void buildWithNullArgument_noException() {
        SearchEngine searchEngine;
        Assertions.assertDoesNotThrow(() -> SearchEngine.builder()
                        .filters(null)
                        .queryDecoder(null)
                        .tokenizer(null)
                        .build(),
                "build with null argument shouldn't throw exception");

    }

    @Test
    public void callConstructor_createInstance() {
        SearchEngine searchEngine = SearchEngine.builder()
                .filters(List.of(new NumberFilter()))
                .queryDecoder(new CommonQueryDecoder())
                .tokenizer(new SpaceTokenizer())
                .build();
        Assertions.assertInstanceOf(SearchEngine.class, searchEngine);
    }




}