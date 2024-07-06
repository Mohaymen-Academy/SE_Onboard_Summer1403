import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
        BufferedReader bufferedReader = null;
        String line;
        StringBuilder content = new StringBuilder();
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            while ((line = bufferedReader.readLine()) != null)
                content.append(line).append("\n");
        } catch (Exception e) {
            System.out.println("cannot read " + file.getName());
        } finally {
            try {
                if (bufferedReader != null)
                    bufferedReader.close();
            } catch (IOException e) {
                System.out.println("cannot close file " + file.getName());
            }
        }

        return content.toString();
    }
}
