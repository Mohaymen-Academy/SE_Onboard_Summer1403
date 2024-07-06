package search_engine.queryDecoder;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public class CommonQueryDecoder implements QueryDecoder {
    @Override
    public Query decode(String query) {
        String[] strings = query.split("\\s+");

        List<String>  includes = new ArrayList<>();
        List<String> optionals = new ArrayList<>();
        List<String>  excludes = new ArrayList<>();

        for (String string : strings) {
            if (string.isEmpty()) {
                continue;
            }
            switch (string.charAt(0)) {
                case '+' -> optionals.add(string.substring(1));
                case '-' ->  excludes.add(string.substring(1));
                default ->  includes.add(string);
            }
        }

        return new Query(ImmutableList.copyOf( includes), ImmutableList.copyOf(optionals), ImmutableList.copyOf( excludes));
    }
}
