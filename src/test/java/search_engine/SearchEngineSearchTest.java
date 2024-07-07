package search_engine;

import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SearchEngineSearchTest {
    List<Document> documents;

    @BeforeEach
    public void setup() {
        documents = new ArrayList<>();
        documents.add(
                Document.builder()
                .id("1")
                .content("some random 6 contents")
                .build()
        );

        documents.add(
                Document.builder()
                .id("2")
                .content("some random other contents")
                .build()
        );

        documents.add(
                Document.builder()
                .id("3")
                .content("this ,\n" +
                        "is a test string")
                .build()
        );

        documents.add(
                Document.builder()
                .id("4")
                .content("other test\n" +
                        "for testing")
                .build()
        );
    }


    @Test
    public void search_nullInput() {
        //given
        SearchEngine searchEngine = SearchEngine.builder().build();
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
    public void search_testcase1() {
        //given
        SearchEngine searchEngine = SearchEngine.builder().build();
        documents.forEach(searchEngine::addDocument);
        String query = "some";
        Set<String> expected = ImmutableSet.of("1", "2");

        //when
        Set<String> results = searchEngine.search(query);

        //then
        Assertions.assertNotNull(results);
        Assertions.assertEquals(expected, results);
    }

    @Test
    public void search_testcase2() {
        //given
        SearchEngine searchEngine = SearchEngine.builder().build();
        documents.forEach(searchEngine::addDocument);
        String query = "+other +random";
        Set<String> expected = ImmutableSet.of("1", "2", "4");

        //when
        Set<String> results = searchEngine.search(query);

        //then
        Assertions.assertNotNull(results);
        Assertions.assertEquals(expected, results);
    }

    @Test
    public void search_testcase3() {
        //given
        SearchEngine searchEngine = SearchEngine.builder().build();
        documents.forEach(searchEngine::addDocument);
        String query = "-for +other +random";
        Set<String> expected = ImmutableSet.of("1", "2");

        //when
        Set<String> results = searchEngine.search(query);

        //then
        Assertions.assertNotNull(results);
        Assertions.assertEquals(expected, results);
    }

    @Test
    public void search_testcase4() {
        //given
        SearchEngine searchEngine = SearchEngine.builder().build();
        documents.forEach(searchEngine::addDocument);
        String query = "-for";
        Set<String> expected = ImmutableSet.of("1", "2", "3");

        //when
        Set<String> results = searchEngine.search(query);

        //then
        Assertions.assertNotNull(results);
        Assertions.assertEquals(expected, results);
    }

    @Test
    public void search_testcase5() {
        //given
        SearchEngine searchEngine = SearchEngine.builder().build();
        documents.forEach(searchEngine::addDocument);
        String query = "+some contents -other";
        Set<String> expected = ImmutableSet.of("1");

        //when
        Set<String> results = searchEngine.search(query);

        //then
        Assertions.assertNotNull(results);
        Assertions.assertEquals(expected, results);
    }


    @Test
    public void search_testcase6() {
        //given
        SearchEngine searchEngine = SearchEngine.builder().build();
        documents.forEach(searchEngine::addDocument);
        String query = "keyboard";
        Set<String> expected = ImmutableSet.of();

        //when
        Set<String> results = searchEngine.search(query);

        //then
        Assertions.assertNotNull(results);
        Assertions.assertEquals(expected, results);
    }

    @Test
    public void search_testcase7() {
        //given
        SearchEngine searchEngine = SearchEngine.builder().build();
        documents.forEach(searchEngine::addDocument);
        String query = "for is";
        Set<String> expected = ImmutableSet.of();

        //when
        Set<String> results = searchEngine.search(query);

        //then
        Assertions.assertNotNull(results);
        Assertions.assertEquals(expected, results);
    }


    @Test
    public void search_testcase8() {
        //given
        SearchEngine searchEngine = SearchEngine.builder().build();
        documents.forEach(searchEngine::addDocument);
        String query = "other test";
        Set<String> expected = ImmutableSet.of("4");

        //when
        Set<String> results = searchEngine.search(query);

        //then
        Assertions.assertNotNull(results);
        Assertions.assertEquals(expected, results);
    }

    @Test
    public void search_testcase9() {
        documents.add(
                Document.builder()
                        .id("")
                        .content("hello ,\n" +
                                "we can test empty id ?")
                        .build()
        );
        //given
        SearchEngine searchEngine = SearchEngine.builder().build();
        documents.forEach(searchEngine::addDocument);
        String query = "can";
        Set<String> expected = ImmutableSet.of("");

        //when
        Set<String> results = searchEngine.search(query);

        //then
        Assertions.assertNotNull(results);
        Assertions.assertEquals(expected, results);
    }

    @Test
    public void search_testcase10() {
        //given
        SearchEngine searchEngine = SearchEngine.builder().build();
        documents.forEach(searchEngine::addDocument);
        String query = "+can";
        Set<String> expected = ImmutableSet.of();

        //when
        Set<String> results = searchEngine.search(query);

        //then
        Assertions.assertNotNull(results);
        Assertions.assertEquals(expected, results);
    }




}
