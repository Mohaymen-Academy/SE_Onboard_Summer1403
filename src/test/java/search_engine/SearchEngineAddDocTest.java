package search_engine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import search_engine.normalizers.Normalizer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

class SearchEngineAddDocTest {

    @Mock
    Normalizer normalizer;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void addDocument_givenNullInput_notThrowException() {
        //given
        SearchEngine searchEngine = SearchEngine.builder()
                .build();

        //then
        assertDoesNotThrow(() -> searchEngine.addDocument(null),
                "addData should not throw NullPointerException");
    }

    @Test
    void addDocument_givenMockNormalizers_invokeNormalizers() {
        //given

        when(normalizer.normalize(anyString())).thenReturn("");

        //when
        SearchEngine searchEngine = SearchEngine.builder()
                .normalizers(List.of(normalizer))
                .build();

        Document document = Document.builder()
                .id("1")
                .content("clean")
                .build();
        searchEngine.addDocument(document);

        //then
        verify(normalizer).normalize(anyString()); 
    }
}
