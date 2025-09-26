package filestatistics.statistics;

import lombok.Getter;

@Getter
public class FileTypeStatistics {

    private final String extension;
    private int fileCount = 0;
    private long totalSizeBytes = 0;
    private int totalLines = 0;
    private int totalNonEmptyLines = 0;
    private int totalCommentLines = 0;

    public FileTypeStatistics(String extension) {
        this.extension = extension;
    }

    public void addFile(long sizeBytes, int totalLines, int nonEmptyLines, int commentLines) {
        fileCount++;
        totalSizeBytes += sizeBytes;
        this.totalLines += totalLines;
        totalNonEmptyLines += nonEmptyLines;
        totalCommentLines += commentLines;
    }

}
