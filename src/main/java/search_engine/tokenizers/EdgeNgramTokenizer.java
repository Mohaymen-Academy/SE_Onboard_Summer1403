package search_engine.tokenizers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
public class EdgeNgramTokenizer implements Tokenizer {

    @Override
    public List<String> tokenize(String str) {
        return getPrefixes(str);
    }

    private List<String> getPrefixes(String str) {
        List<String> tokens = new ArrayList<>(str.length());

        IntStream.range(0, str.length())
                .forEach(i ->
                        tokens.add(str.substring(0, i)));
        return tokens;
    }
}
