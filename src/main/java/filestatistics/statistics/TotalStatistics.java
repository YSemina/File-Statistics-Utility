package filestatistics.statistics;

import lombok.Getter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class TotalStatistics {

    private final Map<String, FileTypeStatistics> byExtension = new ConcurrentHashMap<>();

    public void addFile(String extension, long sizeBytes, int totalLines,
                        int nonEmptyLines, int commentLines) {
        byExtension.computeIfAbsent(extension, ext -> new FileTypeStatistics())
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
        StringBuilder plain = new StringBuilder()
                .append("FILE STATISTICS SUMMARY\n")
                .append("Total files: ").append(getTotalFiles()).append("\n")
                .append("Total size: ").append(getTotalSizeBytes()).append(" bytes\n")
                .append("Total lines: ").append(getTotalLines()).append("\n")
                .append("Non-empty lines: ").append(getTotalNonEmptyLines()).append("\n")
                .append("Comment lines: ").append(getTotalCommentLines()).append("\n\n");

        System.out.print(plain);
    }

    public void printXmlSummary() {
        StringBuilder xml = new StringBuilder()
                .append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
                .append("<fileStatistics>\n")
                .append("  <summary>\n")
                .append("    <totalFiles>").append(getTotalFiles()).append("</totalFiles>\n")
                .append("    <totalSizeBytes>").append(getTotalSizeBytes()).append("</totalSizeBytes>\n")
                .append("    <totalLines>").append(getTotalLines()).append("</totalLines>\n")
                .append("    <totalNonEmptyLines>").append(getTotalNonEmptyLines()).append("</totalNonEmptyLines>\n")
                .append("    <totalCommentLines>").append(getTotalCommentLines()).append("</totalCommentLines>\n")
                .append("  </summary>\n")
                .append("</fileStatistics>");

        System.out.println(xml);
    }

    public void printJsonSummary() {
        StringBuilder json = new StringBuilder()
                .append("{\n")
                .append("  \"summary\": {\n")
                .append("    \"totalFiles\": ").append(getTotalFiles()).append(",\n")
                .append("    \"totalSizeBytes\": ").append(getTotalSizeBytes()).append(",\n")
                .append("    \"totalLines\": ").append(getTotalLines()).append(",\n")
                .append("    \"totalNonEmptyLines\": ").append(getTotalNonEmptyLines()).append(",\n")
                .append("    \"totalCommentLines\": ").append(getTotalCommentLines()).append("\n")
                .append("  }\n")
                .append("}");

        System.out.println(json);
    }

}
