package filestatistics.processor;

import filestatistics.statistics.TotalStatistics;
import java.nio.file.Path;
import java.util.List;

public class FileTypeHandler {

    private final FileReader fileReader;
    private final FileAnalyzer fileAnalyzer;

    public FileTypeHandler(FileReader fileReader, FileAnalyzer fileAnalyzer) {
        this.fileReader = fileReader;
        this.fileAnalyzer = fileAnalyzer;
    }

    public void handleBinaryFile(String extension, long size, TotalStatistics statistics) {
        statistics.addFile(extension, size, 0, 0, 0);
    }

    public void handleScriptFile(Path path, String extension, long size, TotalStatistics statistics) {
        List<String> lines = fileReader.readLines(path);
        FileAnalyzer.FileAnalysisResult analysis = fileAnalyzer.analyzeScript(lines, extension);
        statistics.addFile(extension, size, analysis.totalLines(),
                analysis.nonEmptyLines(), analysis.commentLines());
    }

    public void handleTextFile(Path file, String extension, long size, TotalStatistics statistics) {
        List<String> lines = fileReader.readLines(file);
        FileAnalyzer.FileAnalysisResult analysis = fileAnalyzer.analyzeText(lines);
        statistics.addFile(extension, size, analysis.totalLines(),
                analysis.nonEmptyLines(), 0);
    }

    public void handleUnknownFile(String extension, long size, TotalStatistics statistics) {
        statistics.addFile(extension, size, 0, 0, 0);
    }

}
