package filestatistics.cli;

import filestatistics.config.AppConfig;
import filestatistics.processor.FileProcessor;
import filestatistics.statistics.TotalStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

@Command(name = "filestats",
        version = "File Stats 1.0.0",
        description = "Utility for collecting file statistics",
        mixinStandardHelpOptions = true)
public class CommandLineArgs implements Callable<Integer> {

    private static final Logger logger = LoggerFactory.getLogger(CommandLineArgs.class);

    @Parameters(index = "0", description = "Path to the directory being analyzed")
    private Path path;

    @Option(names = "--include-ext", description = "Include only the specified extensions (comma separated)")
    private String includeExt;

    @Option(names = "--exclude-ext", description = "Exclude the specified extensions (comma separated)")
    private String excludeExt;

    @Override
    public Integer call() throws Exception {
        logger.info("Start of catalog processing: {}", path);

        try {
            validateParameters();
            AppConfig config = createConfig();
            processWithConfig(config);
            return 0;
        } catch (IllegalArgumentException e) {
            logger.error("Error in parameters: {}", e.getMessage());
            return 1;
        } catch (Exception e) {
            logger.error("Error while processing: {}", e.getMessage(), e);
            return 2;
        }
    }

    private void validateParameters() {
        if (!path.toFile().exists()) {
            throw new IllegalArgumentException("Каталог не существует: " + path);
        }
        if (!path.toFile().isDirectory()) {
            throw new IllegalArgumentException("Указанный путь не является каталогом: " + path);
        }
    }

    private AppConfig createConfig() {
        return AppConfig.builder()
                .path(path)
                .includeExtensions(parseExtensions(includeExt))
                .excludeExtensions(parseExtensions(excludeExt))
                .build();
    }

    private List<String> parseExtensions(String extensions) {
        if (extensions == null || extensions.trim().isEmpty()) {
            return List.of();
        }
        return Arrays.stream(extensions.split(","))
                .map(String::trim)
                .map(ext -> ext.startsWith(".") ? ext.substring(1) : ext)
                .toList();
    }

    private void processWithConfig(AppConfig config) {
        logger.info("Start processing with configuration: {}", config);

        try {
            FileProcessor processor = new FileProcessor();
            TotalStatistics statistics = processor.processDirectory(config);
            statistics.printSummary();
            logger.info("Processing completed successfully");
        } catch (IOException e) {
            logger.error("Error processing files: {}", e.getMessage(), e);
            throw new RuntimeException("File processing failed", e);
        }
    }

}
