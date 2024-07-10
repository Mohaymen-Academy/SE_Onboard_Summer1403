package search_engine;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Document {
    private String id;
    private String content;
}
