package filestatistics.processor;

import filestatistics.config.AppConfig;
import filestatistics.statistics.TotalStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class FileProcessor {

    private static final Logger logger = LoggerFactory.getLogger(FileProcessor.class);

    private final FileTypeHandler fileTypeHandler;

    public FileProcessor() {
        this.fileTypeHandler = new FileTypeHandler(new FileReader(), new FileAnalyzer());
    }

    public TotalStatistics processDirectory(AppConfig config) throws IOException {
        TotalStatistics statistics = new TotalStatistics();

        try (Stream<Path> filesStream = Files.list(config.getPath())) {
            filesStream.filter(Files::isRegularFile)
                    .filter(path -> shouldProcessByExtension(path, config))
                    .forEach(file -> processFileSafe(file, statistics));
        }

        return statistics;
    }

    private boolean shouldProcessByExtension(Path path, AppConfig config) {
        String extension = FileUtils.getExtension(path);

        if (!config.getExcludeExtensions().isEmpty() &&
                config.getExcludeExtensions().contains(extension)) {
            return false;
        }

        return config.getIncludeExtensions().isEmpty() ||
                config.getIncludeExtensions().contains(extension);
    }

    private void processFileSafe(Path path, TotalStatistics statistics) {
        try {
            processSingleFile(path, statistics);
        } catch (Exception e) {
            logger.warn("Skipping file due to error: {} - {}", path.getFileName(), e.getMessage());
        }
    }

    private void processSingleFile(Path path, TotalStatistics statistics) throws IOException {
        long size = Files.size(path);
        String extension = FileUtils.getExtension(path);

        logger.debug("Processing file: {}, extension: {}, size: {} bytes",
                path.getFileName(), extension, size);

        if (FileTypeDetector.BINARY_EXTENSIONS.contains(extension)) {
            fileTypeHandler.handleBinaryFile(extension, size, statistics);
        }
        else if (FileTypeDetector.SCRIPT_EXTENSIONS.contains(extension)) {
            fileTypeHandler.handleScriptFile(path, extension, size, statistics);
        }
        else if (FileTypeDetector.TEXT_EXTENSIONS.contains(extension)) {
            fileTypeHandler.handleTextFile(path, extension, size, statistics);
        }
        else {
            fileTypeHandler.handleUnknownFile(extension, size, statistics);
        }
    }

}