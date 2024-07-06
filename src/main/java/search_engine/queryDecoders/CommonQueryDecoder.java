package search_engine.queryDecoders;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public class CommonQueryDecoder implements QueryDecoder {
    @Override
    public Query decode(String query) {
        String[] strings = query.split("\\s+");

        List<String> compulsories = new ArrayList<>();
        List<String> optionals = new ArrayList<>();
        List<String> forbidden = new ArrayList<>();

        for (String string : strings) {
            if (string.isEmpty()) {
                continue;
            }
            switch (string.charAt(0)) {
                case '+' -> optionals.add(string.substring(1));
                case '-' -> forbidden.add(string.substring(1));
                default -> compulsories.add(string);
            }
        }

        return new Query(ImmutableList.copyOf(compulsories), ImmutableList.copyOf(optionals), ImmutableList.copyOf(forbidden));
    }
}
