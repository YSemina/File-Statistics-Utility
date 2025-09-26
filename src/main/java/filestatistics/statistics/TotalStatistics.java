package filestatistics.statistics;

import lombok.Getter;
import java.util.HashMap;
import java.util.Map;

@Getter
public class TotalStatistics {

    private final Map<String, FileTypeStatistics> byExtension = new HashMap<>();

    public void addFile(String extension, long sizeBytes, int totalLines,
                        int nonEmptyLines, int commentLines) {
        byExtension.computeIfAbsent(extension, FileTypeStatistics::new)
                .addFile(sizeBytes, totalLines, nonEmptyLines, commentLines);
    }

    public int getTotalFiles() {
        return byExtension.values().stream()
                .mapToInt(FileTypeStatistics::getFileCount)
                .sum();
    }

    public int getTotalLines() {
        return byExtension.values().stream()
                .mapToInt(FileTypeStatistics::getTotalLines)
                .sum();
    }

    public int getTotalNonEmptyLines() {
        return byExtension.values().stream()
                .mapToInt(FileTypeStatistics::getTotalNonEmptyLines)
                .sum();
    }

    public int getTotalCommentLines() {
        return byExtension.values().stream()
                .mapToInt(FileTypeStatistics::getTotalCommentLines)
                .sum();
    }

    public long getTotalSizeBytes() {
        return byExtension.values().stream()
                .mapToLong(FileTypeStatistics::getTotalSizeBytes)
                .sum();
    }

    public void printSummary() {
        System.out.println("FILE STATISTICS SUMMARY");
        System.out.printf("Total files: %,d%n", getTotalFiles());
        System.out.printf("Total size: %,d bytes%n", getTotalSizeBytes());
        System.out.printf("Total lines: %,d%n", getTotalLines());
        System.out.printf("Non-empty lines: %,d%n", getTotalNonEmptyLines());
        System.out.printf("Comment lines: %,d%n", getTotalCommentLines());
        System.out.println();
    }

}
