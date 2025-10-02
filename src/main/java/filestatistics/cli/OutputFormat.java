package filestatistics.cli;

import lombok.Getter;
import picocli.CommandLine;

@Getter
public enum OutputFormat {

    PLAIN("plain"),
    XML("xml"),
    JSON("json");

    private final String displayName;

    OutputFormat(String displayName) {
        this.displayName = displayName;
    }

    static class OutputFormatConverter implements CommandLine.ITypeConverter<OutputFormat> {
        @Override
        public OutputFormat convert(String value) throws Exception {
            try {
                return OutputFormat.valueOf(value.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new CommandLine.TypeConversionException("Unknown output format: '" + value
                        + "'. Acceptable values: plain, xml, json.");
            }
        }
    }

}
