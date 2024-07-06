package search_engine;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import search_engine.decoders.CommonDecoder;
import search_engine.tokenizers.SpaceTokenizer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

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


    // check constructor





    // check addData

    @Test
    public void addData_givenNullInput_notThrowException() {
        assertDoesNotThrow(() -> underTest.addData(null),
                "addData should not throw NullPointerException");
    }








    // check search

    @Test
    void search() {
    }

}