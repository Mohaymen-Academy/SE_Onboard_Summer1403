package search_engine.normalizers;

public class LowerCaseNormalizer implements Normalizer {
    @Override
    public String normalize(String str) {
        return str.toLowerCase();
    }
}
