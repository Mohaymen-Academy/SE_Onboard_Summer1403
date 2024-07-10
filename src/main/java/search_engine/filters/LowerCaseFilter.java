package search_engine.filters;

public class LowerCaseFilter implements Filter {
    @Override
    public String filter(String str) {
        return str.toLowerCase();
    }
}
