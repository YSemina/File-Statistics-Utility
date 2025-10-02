package filestatistics.cli;

import filestatistics.service.StatisticsService;
import lombok.Getter;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import java.nio.file.Path;
import java.util.concurrent.Callable;

@Command(name = "filestats",
        version = "File Stats 1.0.0",
        description = "Utility for collecting file statistics",
        mixinStandardHelpOptions = true)
@Getter
public class CommandLineArgs implements Callable<Integer> {

    @Parameters(index = "0", description = "Path to the directory being analyzed")
    private Path path;

    @Option(names = {"--output", "-o"},
            description = "Output format: plain, xml, json. Default: ${DEFAULT-VALUE}",
            defaultValue = "plain",
            converter = OutputFormat.OutputFormatConverter.class)
    private OutputFormat outputFormat;

    @Option(names = "--include-ext", description = "Include only the specified extensions (comma separated)")
    private String includeExt;

    @Option(names = "--exclude-ext", description = "Exclude the specified extensions (comma separated)")
    private String excludeExt;

    @Override
    public Integer call() throws Exception {
        StatisticsService statisticsService = new StatisticsService();
        return statisticsService.execute(this);
    }

}
