package search_engine;

import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

public class SearchEngineSearchTest {
    SearchEngine searchEngine;

    @BeforeEach
    public void setup() {
        searchEngine = SearchEngine.builder().build();

        List<Document> documents = List.of(
                Document.builder()
                        .id("1")
                        .content("some random 6 contents")
                        .build(),
                Document.builder()
                        .id("2")
                        .content("some random other contents")
                        .build(),
                Document.builder()
                        .id("3")
                        .content("this ,\n" + "is a test string")
                        .build(),
                Document.builder()
                        .id("4")
                        .content("other test\n" + "for testing")
                        .build()
        );

        documents.forEach(searchEngine::addDocument);
    }


    @Test
    public void search_nullInput() {
        //given
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

        //given
        searchEngine.addDocument(
                Document.builder()
                        .id("")
                        .content("hello ,\n" + "we can test empty id ?")
                        .build()
        );

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
        String query = "+can";
        Set<String> expected = ImmutableSet.of();

        //when
        Set<String> results = searchEngine.search(query);

        //then
        Assertions.assertNotNull(results);
        Assertions.assertEquals(expected, results);
    }


    @Test
    public void search_testcase11() {

        //given
        searchEngine.addDocument(
                Document.builder()
                        .id("")
                        .content(null)
                        .build()
        );

        String query = "can";
        Set<String> expected = ImmutableSet.of();

        //when
        Set<String> results = searchEngine.search(query);

        //then
        Assertions.assertNotNull(results);
        Assertions.assertEquals(expected, results);
    }

    @Test
    public void search_testcase12() {

        //given
        searchEngine.addDocument(
                Document.builder()
                        .id("7")
                        .content("")
                        .build()
        );

        String query = "can";
        Set<String> expected = ImmutableSet.of();

        //when
        Set<String> results = searchEngine.search(query);

        //then
        Assertions.assertNotNull(results);
        Assertions.assertEquals(expected, results);
    }


}
