package search_engine.normalizers;

public class NumberNormalizer implements Normalizer {
    @Override
    public String normalize(String str) {
        return str.replaceAll("[+|-]?\\d+", "");
    }
}
