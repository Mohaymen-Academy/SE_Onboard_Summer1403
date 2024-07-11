package search_engine;

import com.google.common.collect.ImmutableSet;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

class SearchEngineSearchTest {
    SearchEngine searchEngine;

    @BeforeEach
    void setup() {
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
    void search_nullInput() {
        //given
        String query = "";
        Set<String> expected = ImmutableSet.of();

        //when
        Set<String> results = searchEngine.search(query);

        //then
        assertThat(results).isNotNull();
        assertThat(results).isEqualTo(expected);
    }


    @Test
    void search_oneInclude() {
        //given
        String query = "some";
        Set<String> expected = ImmutableSet.of("1", "2");

        //when
        Set<String> results = searchEngine.search(query);

        //then
        assertThat(results).isNotNull();
        assertThat(results).isEqualTo(expected);
    }

    @Test
    void search_twoOptionals() {
        //given
        String query = "+other +random";
        Set<String> expected = ImmutableSet.of("1", "2", "4");

        //when
        Set<String> results = searchEngine.search(query);

        //then
        assertThat(results).isNotNull();
        assertThat(results).isEqualTo(expected);
    }

    @Test
    void search_twoOptionals_oneExclude() {
        //given
        String query = "-for +other +random";
        Set<String> expected = ImmutableSet.of("1", "2");

        //when
        Set<String> results = searchEngine.search(query);

        //then
        assertThat(results).isNotNull();
        assertThat(results).isEqualTo(expected);
    }

    @Test
    void search_OneExclude() {
        //given
        String query = "-for";
        Set<String> expected = ImmutableSet.of("1", "2", "3");

        //when
        Set<String> results = searchEngine.search(query);

        //then
        assertThat(results).isNotNull();
        assertThat(results).isEqualTo(expected);
    }

    @Test
    void search_oneInclude_oneOptional_oneExclude() {
        //given
        String query = "+some contents -other";
        Set<String> expected = ImmutableSet.of("1");

        //when
        Set<String> results = searchEngine.search(query);

        //then
        assertThat(results).isNotNull();
        assertThat(results).isEqualTo(expected);
    }


    @Test
    void search_notFound_oneInclude() {
        //given
        String query = "keyboard";
        Set<String> expected = ImmutableSet.of();

        //when
        Set<String> results = searchEngine.search(query);

        //then
        assertThat(results).isNotNull();
        assertThat(results).isEqualTo(expected);
    }

    @Test
    void search_notFound_twoIncludes() {
        //given
        String query = "for is";
        Set<String> expected = ImmutableSet.of();

        //when
        Set<String> results = searchEngine.search(query);

        //then
        assertThat(results).isNotNull();
        assertThat(results).isEqualTo(expected);
    }


    @Test
    void search_twoIncludes() {
        //given
        String query = "other test";
        Set<String> expected = ImmutableSet.of("4");

        //when
        Set<String> results = searchEngine.search(query);

        //then
        assertThat(results).isNotNull();
        assertThat(results).isEqualTo(expected);
    }

    @Test
    void search_emptyId() {

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
        assertThat(results).isNotNull();
        assertThat(results).isEqualTo(expected);
    }

    @Test
    void search_notFound_oneOptional() {
        //given
        String query = "+can";
        Set<String> expected = ImmutableSet.of();

        //when
        Set<String> results = searchEngine.search(query);

        //then
        assertThat(results).isNotNull();
        assertThat(results).isEqualTo(expected);
    }


    @Test
    void search_emptyId_nullContent() {

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
        assertThat(results).isNotNull();
        assertThat(results).isEqualTo(expected);
    }

    @Test
    void search_emptyContent() {

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
        assertThat(results).isNotNull();
        assertThat(results).isEqualTo(expected);
    }
}
