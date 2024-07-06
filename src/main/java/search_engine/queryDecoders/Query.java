package search_engine.queryDecoders;

import com.google.common.collect.ImmutableList;

import java.util.List;

public record Query(List<String> compulsories, List<String> optionals,
                    List<String> forbidden) {
}
