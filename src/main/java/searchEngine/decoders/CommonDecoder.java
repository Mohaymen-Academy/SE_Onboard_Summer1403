package searchEngine.decoders;

import java.util.Vector;

public class CommonDecoder implements Decoder {
    @Override
    public Query decode(String query) {
        String[] strings = query.split("\\s+");

        Vector<String> compulsories = new Vector<>();
        Vector<String> optionals = new Vector<>();
        Vector<String> forbidden = new Vector<>();

        for (String string : strings) {
            if (string.equals("")) {
                continue;
            }
            switch (string.charAt(0)) {
                case '+' -> optionals.add(string.substring(1));
                case '-' -> forbidden.add(string.substring(1));
                default -> compulsories.add(string);
            }
        }

        return new Query(compulsories, optionals, forbidden);
    }
}
