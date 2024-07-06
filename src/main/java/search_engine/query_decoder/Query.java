package search_engine.query_decoder;

import java.util.List;

public record Query(
        List<String> includes,
        List<String> optionals,
        List<String> excludes
) {
}
