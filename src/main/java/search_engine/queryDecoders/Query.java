package search_engine.queryDecoders;

import com.google.common.collect.ImmutableList;

public record Query(ImmutableList<String> compulsories, ImmutableList<String> optionals,
                    ImmutableList<String> forbidden) {
}
