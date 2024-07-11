package search_engine.tokenizers;

import com.google.common.collect.ImmutableSet;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import search_engine.Document;
import search_engine.SearchEngine;

import java.util.List;
import java.util.Set;

class EdgeNgramTokenizerTest {

    List<Document> documents;

    @BeforeEach
    void setUp() {
        documents = List.of(
                Document.builder()
                        .id("1")
                        .content("omid heydari")
                        .build(),
                Document.builder()
                        .id("2")
                        .content("ali sadeghi")
                        .build(),
                Document.builder()
                        .id("3")
                        .content("sajjad soltanian")
                        .build(),
                Document.builder()
                        .id("4")
                        .content("mohammad Aghaee")
                        .build(),
                Document.builder()
                        .id("5")
                        .content("amin sharifi")
                        .build(),
                Document.builder()
                        .id("6")
                        .content("alireza")
                        .build()
        );
    }


    @Test
    void tokenize_givenStrings() {
        // given
        EdgeNgramTokenizer tokenizer = new EdgeNgramTokenizer();
        String string1 = "omid";
        String string2 = "ali";
        String string3 = "o s";
        String string4 = "";

        List<String> excepted1 = List.of("o", "om", "omi", "omid");
        List<String> excepted2 = List.of("a", "al", "ali");
        List<String> excepted3 = List.of("o", "o ", "o s");
        List<String> excepted4 = List.of();

        // when
        List<String> actual1 = tokenizer.tokenize(string1);
        List<String> actual2 = tokenizer.tokenize(string2);
        List<String> actual3 = tokenizer.tokenize(string3);
        List<String> actual4 = tokenizer.tokenize(string4);

        // then

        assertThat(actual1).isNotNull();
        assertThat(actual2).isNotNull();
        assertThat(actual3).isNotNull();
        assertThat(actual4).isNotNull();

        assertThat(actual1).isEqualTo(excepted1);
        assertThat(actual2).isEqualTo(excepted2);
        assertThat(actual3).isEqualTo(excepted3);
        assertThat(actual4).isEqualTo(excepted4);
    }


    @Test
    void search_edgeNgram_oneLetter() {
        //given
        SearchEngine searchEngine = SearchEngine.builder()
                .tokenizer(new EdgeNgramTokenizer())
                .build();
        documents.forEach(searchEngine::addDocument);
        String query = "a";
        Set<String> expected = ImmutableSet.of("2", "5", "6");

        //when
        Set<String> results = searchEngine.search(query);

        //then
        assertThat(results).isNotNull();
        assertThat(results).isEqualTo(expected);
    }

    @Test
    void search_edgeNgram_completeName() {
        //given
        SearchEngine searchEngine = SearchEngine.builder()
                .tokenizer(new EdgeNgramTokenizer())
                .build();
        documents.forEach(searchEngine::addDocument);
        String query = "omid";
        Set<String> expected = ImmutableSet.of("1");

        //when
        Set<String> results = searchEngine.search(query);

        //then
        assertThat(results).isNotNull();
        assertThat(results).isEqualTo(expected);
    }

    @Test
    void search_edgeNgram_emptyString() {
        //given
        SearchEngine searchEngine = SearchEngine.builder()
                .tokenizer(new EdgeNgramTokenizer())
                .build();
        documents.forEach(searchEngine::addDocument);
        String query = "";
        Set<String> expected = ImmutableSet.of();

        //when
        Set<String> results = searchEngine.search(query);

        //then
        assertThat(results).isNotNull();
        assertThat(results).isEqualTo(expected);
    }

    @Test
    void search_edgeNgram_inputString_emptyResult() {
        //given
        SearchEngine searchEngine = SearchEngine.builder()
                .tokenizer(new EdgeNgramTokenizer())
                .build();
        documents.forEach(searchEngine::addDocument);
        String query = "min";
        Set<String> expected = ImmutableSet.of();

        //when
        Set<String> results = searchEngine.search(query);

        //then
        assertThat(results).isNotNull();
        assertThat(results).isEqualTo(expected);
    }
}