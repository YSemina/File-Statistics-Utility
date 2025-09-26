package filestatistics.processor;

import java.util.Set;

public class FileTypeDetector {

    public static final Set<String> TEXT_EXTENSIONS = Set.of(
            "txt", "java", "js", "py", "sh", "bash", "html", "css", "xml", "json",
            "md", "properties", "yml", "yaml", "csv", "log", "cfg", "conf"
    );

    public static final Set<String> BINARY_EXTENSIONS = Set.of(
            "jpg", "jpeg", "png", "gif", "pdf", "zip", "rar", "exe", "dll"
    );

    public static final Set<String> SCRIPT_EXTENSIONS = Set.of(
            "java", "js", "sh", "bash", "py", "cfg", "conf", "properties"
    );

    private FileTypeDetector() {}

}
