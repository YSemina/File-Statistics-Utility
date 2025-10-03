package filestatistics.processor;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class FileUtils {

    public static String getExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1 || dotIndex == fileName.length() - 1)
                ? "no_extension"
                : fileName.substring(dotIndex + 1).toLowerCase();
    }

    public static String getExtension(Path path) {
        return getExtension(path.getFileName().toString());
    }

    public static List<String> splitLines(String content) {
        if (content == null || content.isEmpty()) {
            return List.of();
        }

        return Arrays.asList(content.split("\\r?\\n|\\r"));
    }

    private FileUtils() {}

}
