package filestatistics.service;

import filestatistics.cli.CommandLineArgs;
import filestatistics.cli.OutputFormat;
import filestatistics.config.AppConfig;
import filestatistics.processor.FileProcessor;
import filestatistics.statistics.TotalStatistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatisticsServiceTest {

    private static final int SUCCESS_EXIT_CODE = 0;
    private static final int VALIDATION_ERROR_CODE = 1;
    private static final int PROCESSING_ERROR_CODE = 2;

    private static final String NON_EXISTENT_PATH = "/non/existent/path";
    private static final String TEST_FILE_NAME = "test_file.txt";

    private static final String PROCESSING_FAILED_MESSAGE = "Processing failed";

    @Mock
    private FileProcessor fileProcessor;

    @Mock
    private ConfigBuilder configBuilder;

    private StatisticsService statisticsService;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        statisticsService = new StatisticsService(fileProcessor, configBuilder);
    }

    @Nested
    @DisplayName("Тесты преобразования файлов")
    class TestsForExecute {

        @Test
        @DisplayName("успешное завершение с кодом 0")
        void execute_SuccessfulProcessing_ReturnsZero() throws Exception {
            // Given
            CommandLineArgs args = createValidArgs(tempDir);
            AppConfig config = createValidConfig(tempDir);
            TotalStatistics statistics = mock(TotalStatistics.class);

            when(configBuilder.buildFromArgs(args)).thenReturn(config);
            when(fileProcessor.processDirectory(config)).thenReturn(statistics);

            // When
            Integer result = statisticsService.execute(args);

            // Then
            assertEquals(SUCCESS_EXIT_CODE, result);
            verify(fileProcessor).processDirectory(config);
            verify(statistics).printSummary();
        }

        @Test
        @DisplayName("с XML форматом выводит XML статистику")
        void execute_WithXmlFormat_CallsXmlPrint() throws Exception {
            // Given
            CommandLineArgs args = createValidArgsWithFormat(tempDir, OutputFormat.XML);
            AppConfig config = createValidConfig(tempDir);
            TotalStatistics statistics = mock(TotalStatistics.class);

            when(configBuilder.buildFromArgs(args)).thenReturn(config);
            when(fileProcessor.processDirectory(config)).thenReturn(statistics);

            // When
            Integer result = statisticsService.execute(args);

            // Then
            assertEquals(SUCCESS_EXIT_CODE, result);
            verify(statistics).printXmlSummary();
            verify(statistics, never()).printSummary();
        }

        @Test
        @DisplayName("с JSON форматом выводит JSON статистику")
        void execute_WithJsonFormat_CallsJsonPrint() throws Exception {
            // Given
            CommandLineArgs args = createValidArgsWithFormat(tempDir, OutputFormat.JSON);
            AppConfig config = createValidConfig(tempDir);
            TotalStatistics statistics = mock(TotalStatistics.class);

            when(configBuilder.buildFromArgs(args)).thenReturn(config);
            when(fileProcessor.processDirectory(config)).thenReturn(statistics);

            // When
            Integer result = statisticsService.execute(args);

            // Then
            assertEquals(SUCCESS_EXIT_CODE, result);
            verify(statistics).printJsonSummary();
        }

        @Test
        @DisplayName("при ошибке обработки возвращает код ошибки 2")
        void execute_DirectoryDoesNotExist_ReturnsErrorCode() throws IOException {
            // Given
            CommandLineArgs args = createValidArgs(tempDir);
            AppConfig config = createValidConfig(tempDir);

            when(configBuilder.buildFromArgs(args)).thenReturn(config);
            when(fileProcessor.processDirectory(config))
                    .thenThrow(new RuntimeException(PROCESSING_FAILED_MESSAGE));

            // When
            Integer result = statisticsService.execute(args);

            // Then
            assertEquals(PROCESSING_ERROR_CODE, result);
        }

        @Test
        @DisplayName("с несуществующей директорией возвращает код ошибки 1")
        void execute_FileProcessorThrowsException_ReturnsErrorCode() {
            // Given
            CommandLineArgs args = createArgsWithNonExistentPath();

            // When
            Integer result = statisticsService.execute(args);

            // Then
            assertEquals(VALIDATION_ERROR_CODE, result);
        }

    }

    @Nested
    @DisplayName("Тесты валидации параметров")
    class TestsForValidateParameters {

        @Test
        @DisplayName("валидная директория принимается")
        void validateParameters_ValidDirectory_ReturnsSuccessCode0() throws IOException {
            // Given
            Path validPath = tempDir;
            CommandLineArgs args = createArgsWithPath(validPath, true);

            AppConfig config = createValidConfig(tempDir);
            when(configBuilder.buildFromArgs(args)).thenReturn(config);
            when(fileProcessor.processDirectory(config)).thenReturn(new TotalStatistics());

            // When
            Integer result = statisticsService.execute(args);

            // Then
            assertEquals(SUCCESS_EXIT_CODE, result);
        }

        @Test
        @DisplayName("при несуществующем пути возвращает код ошибки 1")
        void validateParameters_NonExistentPath_ReturnsErrorCode1() {
            // Given
            CommandLineArgs args = createArgsWithNonExistentPath();

            // When
            Integer result = statisticsService.execute(args);

            // Then
            assertEquals(VALIDATION_ERROR_CODE, result);
        }

        @Test
        @DisplayName("при передаче файла вместо директории возвращает код ошибки 1")
        void validateParameters_FileInsteadOfDirectory_ReturnsErrorCode1() throws IOException {
            // Given
            Path filePath = createTestFile();
            CommandLineArgs args = createArgsWithPath(filePath, false);

            // When
            Integer result = statisticsService.execute(args);

            // Then
            assertEquals(VALIDATION_ERROR_CODE, result);
        }

    }

    private CommandLineArgs createValidArgs(Path path) {
        return createValidArgsWithFormat(path, OutputFormat.PLAIN);
    }

    private CommandLineArgs createValidArgsWithFormat(Path path, OutputFormat format) {
        CommandLineArgs args = mock(CommandLineArgs.class);
        when(args.getPath()).thenReturn(path);
        when(args.getOutputFormat()).thenReturn(format);
        return args;
    }

    private CommandLineArgs createArgsWithNonExistentPath() {
        CommandLineArgs args = mock(CommandLineArgs.class);
        when(args.getPath()).thenReturn(Path.of(NON_EXISTENT_PATH));
        return args;
    }

    private CommandLineArgs createArgsWithPath(Path path, boolean setupOutputFormat) {
        CommandLineArgs args = mock(CommandLineArgs.class);
        when(args.getPath()).thenReturn(path);
        if (setupOutputFormat) {
            when(args.getOutputFormat()).thenReturn(OutputFormat.PLAIN);
        }
        return args;
    }

    private AppConfig createValidConfig(Path path) {
        return AppConfig.builder()
                .path(path)
                .build();
    }

    private Path createTestFile() throws IOException {
        return Files.createFile(tempDir.resolve(TEST_FILE_NAME));
    }

}
