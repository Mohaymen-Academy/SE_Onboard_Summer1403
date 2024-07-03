package search_engine.decoders;

public interface Decoder {
    Query decode(String query);
}
