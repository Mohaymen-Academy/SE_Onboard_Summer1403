package searchEngine.decoders;

public interface Decoder {
    Query decode(String query);
}
