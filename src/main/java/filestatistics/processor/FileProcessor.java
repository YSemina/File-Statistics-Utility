package filestatistics.processor;

import filestatistics.config.AppConfig;
import filestatistics.statistics.TotalStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileProcessor {

    private static final Logger logger = LoggerFactory.getLogger(FileProcessor.class);

    private final FileTypeHandler fileTypeHandler;

    public FileProcessor() {
        this.fileTypeHandler = new FileTypeHandler(new FileReader(), new FileAnalyzer());
    }

    public TotalStatistics processDirectory(AppConfig config) throws IOException {
        TotalStatistics statistics = new TotalStatistics();

        Files.list(config.getPath())
                .filter(Files::isRegularFile)
                .filter(file -> shouldProcessFile(file, config))
                .forEach(file -> processFileSafe(file, statistics));

        return statistics;
    }

    private boolean shouldProcessFile(Path file, AppConfig config) {
        String extension = FileUtils.getExtension(file);

        if (!config.getExcludeExtensions().isEmpty() &&
                config.getExcludeExtensions().contains(extension)) {
            return false;
        }

        return config.getIncludeExtensions().isEmpty() ||
                config.getIncludeExtensions().contains(extension);
    }

    private void processFileSafe(Path file, TotalStatistics statistics) {
        try {
            processSingleFile(file, statistics);
        } catch (Exception e) {
            logger.warn("Skipping file due to error: {} - {}", file.getFileName(), e.getMessage());
        }
    }

    private void processSingleFile(Path file, TotalStatistics statistics) throws IOException {
        long size = Files.size(file);
        String extension = FileUtils.getExtension(file.getFileName().toString());

        logger.debug("Processing file: {}, extension: {}, size: {} bytes",
                file.getFileName(), extension, size);

        if (FileTypeDetector.BINARY_EXTENSIONS.contains(extension)) {
            fileTypeHandler.handleBinaryFile(extension, size, statistics);
        }
        else if (FileTypeDetector.SCRIPT_EXTENSIONS.contains(extension)) {
            fileTypeHandler.handleScriptFile(file, extension, size, statistics);
        }
        else if (FileTypeDetector.TEXT_EXTENSIONS.contains(extension)) {
            fileTypeHandler.handleTextFile(file, extension, size, statistics);
        }
        else {
            fileTypeHandler.handleUnknownFile(extension, size, statistics);
        }
    }

}