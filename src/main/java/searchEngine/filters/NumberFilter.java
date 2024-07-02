package searchEngine.filters;

public class NumberFilter implements Filter {
    @Override
    public String doFilter(String str) {
        return str.replaceAll("[+|-]?[0-9]+", "");
    }
}
