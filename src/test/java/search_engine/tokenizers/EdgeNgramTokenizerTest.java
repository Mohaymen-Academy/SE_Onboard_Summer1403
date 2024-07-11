package search_engine.tokenizers;

import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.Assertions;
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

        Assertions.assertNotNull(actual1);
        Assertions.assertNotNull(actual2);
        Assertions.assertNotNull(actual3);
        Assertions.assertNotNull(actual4);

        Assertions.assertEquals(excepted1, actual1);
        Assertions.assertEquals(excepted2, actual2);
        Assertions.assertEquals(excepted3, actual3);
        Assertions.assertEquals(excepted4, actual4);


    }


    @Test
    void search_edgeNgram_testcase1() {
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
        Assertions.assertNotNull(results);
        Assertions.assertEquals(expected, results);
    }

    @Test
    void search_edgeNgram_testcase2() {
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
        Assertions.assertNotNull(results);
        Assertions.assertEquals(expected, results);
    }

    @Test
    void search_edgeNgram_testcase3() {
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
        Assertions.assertNotNull(results);
        Assertions.assertEquals(expected, results);
    }

    @Test
    void search_edgeNgram_testcase4() {
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
        Assertions.assertNotNull(results);
        Assertions.assertEquals(expected, results);
    }


}