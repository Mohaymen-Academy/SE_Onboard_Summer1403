package search_engine;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import search_engine.normalizers.Normalizer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchEngineAddDocTest {

    @Mock
    Normalizer normalizer;

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
