package filestatistics.service;

import filestatistics.cli.CommandLineArgs;
import filestatistics.config.AppConfig;
import filestatistics.processor.FileProcessor;
import filestatistics.statistics.TotalStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.file.Path;

public class StatisticsService {

    private static final Logger logger = LoggerFactory.getLogger(StatisticsService.class);

    private final FileProcessor fileProcessor;
    private final ConfigBuilder configBuilder;

    public StatisticsService(FileProcessor fileProcessor, ConfigBuilder configBuilder) {
        this.fileProcessor = fileProcessor;
        this.configBuilder = configBuilder;
    }

    public StatisticsService() {
        this(new FileProcessor(), new ConfigBuilder());
    }

    public Integer execute(CommandLineArgs args) {
        logger.info("Start of catalog processing: {}", args.getPath());

        try {
            validateParameters(args.getPath());
            AppConfig config = configBuilder.buildFromArgs(args);
            return processWithConfig(config);

        } catch (IllegalArgumentException e) {
            logger.error("Error in parameters: {}", e.getMessage());
            return 1;
        } catch (Exception e) {
            logger.error("Error while processing: {}", e.getMessage(), e);
            return 2;
        }
    }

    private void validateParameters(Path path) {
        if (!path.toFile().exists()) {
            throw new IllegalArgumentException("Каталог не существует: " + path);
        }
        if (!path.toFile().isDirectory()) {
            throw new IllegalArgumentException("Указанный путь не является каталогом: " + path);
        }
    }

    private Integer processWithConfig(AppConfig config) {
        logger.info("Start processing with configuration: {}", config);

        try {
            TotalStatistics statistics = fileProcessor.processDirectory(config);
            statistics.printSummary();
            logger.info("Processing completed successfully");
            return 0;
        } catch (Exception e) {
            logger.error("Error processing files: {}", e.getMessage(), e);
            throw new RuntimeException("File processing failed", e);
        }
    }

}
