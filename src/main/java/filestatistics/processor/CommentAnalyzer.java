package filestatistics.processor;

import java.util.Map;

public class CommentAnalyzer {

    private static final Map<String, String> COMMENT_SYMBOLS = Map.of(
            "java", "//",
            "js", "//",
            "sh", "#",
            "bash", "#",
            "py", "#",
            "cfg", "#",
            "conf", "#",
            "properties", "#"
    );

    public static boolean isCommentLine(String trimmedLine, String fileExtension) {
        String commentSymbol = COMMENT_SYMBOLS.get(fileExtension);
        if (commentSymbol == null) {
            return false;
        }

        return trimmedLine.startsWith(commentSymbol);
    }

}
