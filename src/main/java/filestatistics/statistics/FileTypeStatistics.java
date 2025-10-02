package filestatistics.statistics;

import lombok.NoArgsConstructor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@NoArgsConstructor
public class FileTypeStatistics {

    private final AtomicInteger fileCount = new AtomicInteger(0);
    private final AtomicLong totalSizeBytes = new AtomicLong(0);
    private final AtomicInteger totalLines = new AtomicInteger(0);
    private final AtomicInteger totalNonEmptyLines = new AtomicInteger(0);
    private final AtomicInteger totalCommentLines = new AtomicInteger(0);

    public int getFileCount() {
        return fileCount.get();
    }

    public long getTotalSizeBytes() {
        return totalSizeBytes.get();
    }

    public int getTotalLines() {
        return totalLines.get();
    }

    public int getTotalNonEmptyLines() {
        return totalNonEmptyLines.get();
    }

    public int getTotalCommentLines() {
        return totalCommentLines.get();
    }

    public void addFile(long sizeBytes, int totalLines, int nonEmptyLines, int commentLines) {
        fileCount.incrementAndGet();
        totalSizeBytes.addAndGet(sizeBytes);
        this.totalLines.addAndGet(totalLines);
        totalNonEmptyLines.addAndGet(nonEmptyLines);
        totalCommentLines.addAndGet(commentLines);
    }

}
