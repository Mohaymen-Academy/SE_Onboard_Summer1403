package search_engine;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;
import search_engine.normalizers.NumberNormalizer;
import search_engine.query_decoder.CommonQueryDecoder;
import search_engine.tokenizers.SpaceTokenizer;

import java.util.List;

class SearchEngineBuilderTest {
    @Test
    public void constructor_givenNoArgument_notThrownException() {
        assertThatCode(() -> SearchEngine.builder().build()).doesNotThrowAnyException();

    }

    @Test
    public void constructor_givenNullArgument_notThrownException() {
        assertThatCode(() -> SearchEngine.builder()
                        .normalizers(null)
                        .queryDecoder(null)
                        .tokenizer(null)
                        .build()).doesNotThrowAnyException();

    }

    @Test
    public void constructor_givenArgs_createInstance() {
        Object object = SearchEngine.builder()
                .normalizers(List.of(new NumberNormalizer()))
                .queryDecoder(new CommonQueryDecoder())
                .tokenizer(new SpaceTokenizer())
                .build();
        assertThat(object).isExactlyInstanceOf(SearchEngine.class);
    }
}