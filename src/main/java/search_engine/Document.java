package search_engine;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Document {
    private String id;
    private String content;
}
