import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileUtilities {
    public static List<File> getFilesByDirPath(String path) {
        File dir = new File(path);
        File[] files = dir.listFiles();
        if (files == null) {
            System.out.println("path isn't a directory");
            return new ArrayList<>();
        }
        return Arrays.stream(files).toList();
    }

    public static String readFileContent(File file) {
        try {
            return Files.readString(file.toPath());
        } catch (IOException e) {
            System.out.println("cannot read file " + file.getName());
        }
        return "";
    }
}
