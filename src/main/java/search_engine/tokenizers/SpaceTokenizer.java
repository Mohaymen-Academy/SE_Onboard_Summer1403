package search_engine.tokenizers;

import java.util.Arrays;
import java.util.List;

public class SpaceTokenizer implements Tokenizer {
    @Override
    public List<String> tokenize(String str) {
        return Arrays.stream(str.split("\\s+")).toList();
    }
}
