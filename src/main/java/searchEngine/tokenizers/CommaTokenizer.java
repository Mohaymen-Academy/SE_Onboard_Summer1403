package searchEngine.tokenizers;

public class CommaTokenizer implements Tokenizer {
    @Override
    public String[] tokenize(String str) {
        return str.split(",");
    }
}
