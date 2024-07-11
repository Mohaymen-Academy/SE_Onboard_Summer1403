import org.apache.commons.collections4.CollectionUtils;
import search_engine.SearchEngine;

import java.util.Collection;
import java.util.Scanner;
import java.util.Set;

public class InputQueryUtilities {
    public static void handleInputs(SearchEngine searchEngine) {
        try (Scanner scanner = new Scanner(System.in) ) {
            String query;
            while (!(query = scanner.nextLine()).equals("q")) {
                Set<String> result = searchEngine.search(query);
                printCollection(result);
            }
        }
    }

    private static void printCollection(Collection<String> collection) {
        if (CollectionUtils.isEmpty(collection)) {
            System.out.println("nothing!");
            return;
        }
        collection.forEach(System.out::println);
    }
}
