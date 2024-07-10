package search_engine.filters;

public class NumberFilter implements Filter {
    @Override
    public String filter(String str) {
        return str.replaceAll("[+|-]?\\d+", "");
    }
}
