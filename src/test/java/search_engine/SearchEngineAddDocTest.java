package search_engine;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class SearchEngineAddDocTest {

    SearchEngine underTest;








    @Test
    public void addData_givenNullInput_notThrowException() {
        assertDoesNotThrow(() -> underTest.addDocument(null),
                "addData should not throw NullPointerException");
    }

}
