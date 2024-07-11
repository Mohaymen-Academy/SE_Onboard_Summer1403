package search_engine.normalizers;

public class PunctuationNormalizer implements Normalizer {

    @Override
    public String normalize(String str) {
        return str.replaceAll("\\p{Punct}", "");
    }
}
