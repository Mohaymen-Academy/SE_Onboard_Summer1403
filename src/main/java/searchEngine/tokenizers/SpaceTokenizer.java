package searchEngine.tokenizers;

public class SpaceTokenizer implements Tokenizer {
    @Override
    public String[] tokenize(String str) {
        return str.split("\\s+");
    }
}
