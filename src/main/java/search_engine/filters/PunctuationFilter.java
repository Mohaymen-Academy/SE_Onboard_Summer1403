package search_engine.filters;

public class PunctuationFilter implements Filter {

    @Override
    public String filter(String str) {
        return str.replaceAll("\\p{Punct}", "");
    }
}
