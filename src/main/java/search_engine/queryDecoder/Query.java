package search_engine.queryDecoder;

import java.util.List;

public record Query(List<String> includes, List<String> optionals,
                    List<String> excludes) {
}
