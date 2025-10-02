package filestatistics.service;

import filestatistics.cli.CommandLineArgs;
import filestatistics.config.AppConfig;
import java.util.Arrays;
import java.util.List;

public class ConfigBuilder {

    public AppConfig buildFromArgs(CommandLineArgs args) {
        return AppConfig.builder()
                .path(args.getPath())
                .includeExtensions(parseExtensions(args.getIncludeExt()))
                .excludeExtensions(parseExtensions(args.getExcludeExt()))
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

}
