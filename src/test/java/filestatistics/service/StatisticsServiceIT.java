package filestatistics.service;

import filestatistics.cli.CommandLineArgs;
import filestatistics.cli.OutputFormat;
import org.junit.jupiter.api.Test;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StatisticsServiceIT {

    private static final String TEMP_DIR_PREFIX = "test_stats";
    private static final String TEST_FILE_NAME = "test1.txt";
    private static final OutputFormat DEFAULT_OUTPUT_FORMAT = OutputFormat.PLAIN;
    private static final int EXPECTED_SUCCESS_CODE = 0;

    private StatisticsService statisticsService;

    @Test
    void execute_IntegrationTestWithRealFiles_ReturnsSuccess() throws Exception {
        // Given
        statisticsService = new StatisticsService();
        CommandLineArgs args = mock(CommandLineArgs.class);

        Path testDir = Files.createTempDirectory(TEMP_DIR_PREFIX);
        Files.createFile(testDir.resolve(TEST_FILE_NAME));

        when(args.getPath()).thenReturn(testDir);
        when(args.getOutputFormat()).thenReturn(DEFAULT_OUTPUT_FORMAT);

        // When
        Integer result = statisticsService.execute(args);

        // Then
        assertEquals(EXPECTED_SUCCESS_CODE, result);

        // Cleanup
        Files.deleteIfExists(testDir.resolve(TEST_FILE_NAME));
        Files.deleteIfExists(testDir);
    }

}
