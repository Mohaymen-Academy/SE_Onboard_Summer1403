package search_engine;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import search_engine.filters.NumberFilter;
import search_engine.query_decoder.CommonQueryDecoder;
import search_engine.tokenizers.SpaceTokenizer;

import java.util.List;

class SearchEngineBuilderTest {
    @Test
    public void buildWithoutArgument_noException() {
        Assertions.assertDoesNotThrow(() -> SearchEngine.builder().build(),
                "build with no argument shouldn't throw exception");

    }

    @Test
    public void buildWithNullArgument_noException() {
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