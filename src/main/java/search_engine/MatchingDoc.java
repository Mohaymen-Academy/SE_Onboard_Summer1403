package search_engine;

import java.util.ArrayList;

public class MatchingDoc {
    private final String id;
    private final ArrayList<Integer> indexes;

    public MatchingDoc(String id) {
        this.id = id;
        this.indexes = new ArrayList<>();
    }

    public void addIndex(int index) {
        indexes.add(index);
    }


}
