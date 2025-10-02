package filestatistics.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public class FileAnalyzer {

    private static final Logger logger = LoggerFactory.getLogger(FileAnalyzer.class);

    public FileAnalysisResult analyzeScript(List<String> lines, String extension) {
        int totalLines = lines.size();
        int nonEmptyLines = 0;
        int commentLines = 0;

        logger.debug("Analyzing {} lines for extension: {}", totalLines, extension);

        for (int i = 0; i < lines.size(); i++) {
            LineAnalysis lineAnalysis = analyzeLine(lines.get(i), extension, i + 1);
            if (lineAnalysis.nonEmpty()) nonEmptyLines++;
            if (lineAnalysis.comment()) commentLines++;
        }

        logger.info("Final results - Total: {}, Non-empty: {}, Comments: {}",
                totalLines, nonEmptyLines, commentLines);

        return new FileAnalysisResult(totalLines, nonEmptyLines, commentLines);
    }

    public FileAnalysisResult analyzeText(List<String> lines) {
        int totalLines = lines.size();
        int nonEmptyLines = 0;

        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                nonEmptyLines++;
            }
        }

        return new FileAnalysisResult(totalLines, nonEmptyLines, 0);
    }

    private LineAnalysis analyzeLine(String line, String extension, int lineNumber) {
        String trimmed = line.trim();
        boolean isEmpty = trimmed.isEmpty();
        boolean isComment = !isEmpty && CommentAnalyzer.isCommentLine(trimmed, extension);

        logger.debug("Line {}: '{}' (trimmed: '{}')", lineNumber, line, trimmed);
        logger.debug("Line {}: {}", lineNumber, isEmpty ? "EMPTY" : isComment ? "COMMENT" : "CODE");

        return new LineAnalysis(isEmpty, isComment);
    }

    private record LineAnalysis(boolean empty, boolean comment) {
        public boolean nonEmpty() { return !empty; }
    }

    public record FileAnalysisResult(int totalLines, int nonEmptyLines, int commentLines) {}

}
