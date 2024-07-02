import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileHandler {
    public static File[] getAllFiles(String path) {
        File dir = new File(path);
        File[] files = dir.listFiles();
        if (files != null) {
            return files;
        } else {
            throw new RuntimeException("path isn't a directory");
        }
    }

    public static String readFileContent(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line.toLowerCase()).append(" ");
        }
        return sb.toString();
    }
}
