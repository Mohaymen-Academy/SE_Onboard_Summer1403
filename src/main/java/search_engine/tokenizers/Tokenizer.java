package search_engine.tokenizers;

import java.util.List;

public interface Tokenizer {
    List<String> tokenize(String str);
}
