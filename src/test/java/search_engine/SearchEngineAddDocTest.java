package search_engine;

import org.junit.jupiter.api.Test;
import search_engine.filters.NumberFilter;
import search_engine.filters.PunctuationFilter;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

public class SearchEngineAddDocTest {

    @Test
    public void addDocument_givenNullInput_notThrowException() {
        //given
        SearchEngine searchEngine = SearchEngine.builder()
                .build();

        //then
        assertDoesNotThrow(() -> searchEngine.addDocument(null),
                "addData should not throw NullPointerException");
    }

    @Test
    public void addDocument_givenMockFilters_invokeFilters() {
        //given
        NumberFilter mockNumberFilter = mock(NumberFilter.class);
        PunctuationFilter mockPunctuationFilter = mock(PunctuationFilter.class);
        when(mockNumberFilter.filter(anyString())).thenReturn("");
        when(mockPunctuationFilter.filter(anyString())).thenReturn("");

        //when
        SearchEngine searchEngine = SearchEngine.builder()
                .filters(List.of(mockNumberFilter, mockPunctuationFilter))
                .build();

        Document document = Document.builder()
                .id("1")
                .content("")
                .build();
        searchEngine.addDocument(document);

        //then
        verify(mockNumberFilter).filter(anyString());
        verify(mockPunctuationFilter).filter(anyString());
    }
}
