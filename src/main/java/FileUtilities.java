import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;

public class FileUtilities {
    public static List<File> getFilesByDirPath(String path) {
        File dir = new File(path);
        File[] files = dir.listFiles();
        if (files == null)
            throw new RuntimeException("path isn't a directory");
        return Arrays.stream(files).toList();
    }

    public static String readFileContent(File file) {
        String line;
        StringBuilder content = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            while ((line = bufferedReader.readLine()) != null)
                content.append(line).append("\n");
        } catch (Exception e) {
            System.out.println("cannot read " + file.getName());
        }

        return content.toString();
    }
}
