package filestatistics;

import filestatistics.cli.CommandLineArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            logger.info("Launching File Stats Utility");
            logger.debug("Command line arguments: {}", (Object) args);

            CommandLineArgs command = new CommandLineArgs();
            CommandLine cmd = new CommandLine(command);

            cmd.setCommandName("./gradlew run --args=\"\"");

            int exitCode = cmd.execute(args);

            if (exitCode == 0) {
                logger.info("The utility completed successfully.");
            } else {
                logger.warn("The utility terminated with an error code: {}", exitCode);
            }

            System.exit(exitCode);

        } catch (Exception e) {
            logger.error("Critical error while executing utility", e);
            System.exit(1);
        }
    }

}