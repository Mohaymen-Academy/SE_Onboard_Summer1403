package searchEngine.decoders;

import java.util.Vector;

public record Query(Vector<String> compulsories, Vector<String> optionals, Vector<String> forbidden) {
}
