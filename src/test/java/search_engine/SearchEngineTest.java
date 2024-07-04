package search_engine;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import search_engine.decoders.CommonDecoder;
import search_engine.tokenizers.SpaceTokenizer;

class SearchEngineTest {

    SearchEngine<String> underTest = SearchEngine.<String>builder()
            .tokenizer(new SpaceTokenizer())
            .decoder(new CommonDecoder()).build();

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addNullData() {

        underTest.addData(null);
    }

    @Test
    void search() {
    }

    @Test
    void builder() {
    }
}