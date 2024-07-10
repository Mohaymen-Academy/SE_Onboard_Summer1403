package search_engine.tokenizers;

import java.util.List;
import java.util.stream.Stream;

public class EdgeNgramTokenizer implements Tokenizer {

    @Override
    public List<String> tokenize(String str) {
        return getPrefixes(str);
    }

    private List<String> getPrefixes(String str) {
        return Stream.iterate(1, i -> i + 1)
                .limit(str.length())
                .map(i -> str.substring(0, i))
                .toList();
    }
}
