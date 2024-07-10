package search_engine;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import search_engine.normalizers.NumberNormalizer;
import search_engine.query_decoder.CommonQueryDecoder;
import search_engine.tokenizers.SpaceTokenizer;

import java.util.List;

class SearchEngineBuilderTest {
    @Test
    public void constructor_givenNoArgument_notThrownException() {
        Assertions.assertDoesNotThrow(() -> SearchEngine.builder().build(),
                "build with no argument shouldn't throw exception");

    }

    @Test
    public void constructor_givenNullArgument_notThrownException() {
        Assertions.assertDoesNotThrow(() -> SearchEngine.builder()
                        .normalizers(null)
                        .queryDecoder(null)
                        .tokenizer(null)
                        .build(),
                "build with null argument shouldn't throw exception");

    }

    @Test
    public void constructor_givenArgs_createInstance() {
        SearchEngine searchEngine = SearchEngine.builder()
                .normalizers(List.of(new NumberNormalizer()))
                .queryDecoder(new CommonQueryDecoder())
                .tokenizer(new SpaceTokenizer())
                .build();
        Assertions.assertInstanceOf(SearchEngine.class, searchEngine);
    }
}