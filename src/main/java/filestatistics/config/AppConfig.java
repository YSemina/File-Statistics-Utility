package filestatistics.config;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import java.nio.file.Path;
import java.util.List;

@Getter
@ToString
@Builder
public class AppConfig {

    private final Path path;
    private final List<String> includeExtensions;
    private final List<String> excludeExtensions;

}
