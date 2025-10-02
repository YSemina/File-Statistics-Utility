package filestatistics.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileReader {

    private static final Logger logger = LoggerFactory.getLogger(FileReader.class);

    public List<String> readLines(Path file) {
        logger.info("Attempting to read file: {}", file.getFileName());

        try {
            byte[] fileBytes = Files.readAllBytes(file);
            Charset charset = detectCharsetByBOM(fileBytes);
            if (charset == null) {
                charset = StandardCharsets.UTF_8;
            }

            logger.info("Detected charset: {}", charset.name());
            return readFileContent(fileBytes, charset);

        } catch (IOException e) {
            logger.error("Error reading file: {}", file.getFileName(), e);
            return List.of();
        }
    }

    private List<String> readFileContent(byte[] fileBytes, Charset charset) {
        String content = new String(fileBytes, charset);

        content = removeBOM(content);

        List<String> lines = charset.name().contains("UTF-16")
                ? readUtf16Content(content)
                : readRegularContent(content);

        logFileContent(lines);
        return lines;
    }

    private List<String> readUtf16Content(String content) {
        List<String> lines = new ArrayList<>();
        StringBuilder currentLine = new StringBuilder();

        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);

            if (c == '\n') {
                lines.add(currentLine.toString());
                currentLine.setLength(0);
            } else if (c == '\r') {
                lines.add(currentLine.toString());
                currentLine.setLength(0);
                if (i + 1 < content.length() && content.charAt(i + 1) == '\n') {
                    i++;
                }
            } else {
                currentLine.append(c);
            }
        }

        if (!currentLine.isEmpty() || content.endsWith("\n") || content.endsWith("\r")) {
            lines.add(currentLine.toString());
        }

        return lines;
    }

    private List<String> readRegularContent(String content) {
        return FileUtils.splitLines(content);
    }

    private String removeBOM(String content) {
        if (!content.isEmpty() && content.charAt(0) == '\uFEFF') {
            return content.substring(1);
        }
        return content;
    }

    private Charset detectCharsetByBOM(byte[] bytes) {
        if (bytes.length >= 2) {
            if (bytes[0] == (byte)0xFF && bytes[1] == (byte)0xFE) {
                return StandardCharsets.UTF_16LE;
            }
            if (bytes[0] == (byte)0xFE && bytes[1] == (byte)0xFF) {
                return StandardCharsets.UTF_16BE;
            }
        }
        if (bytes.length >= 3 && bytes[0] == (byte)0xEF && bytes[1] == (byte)0xBB && bytes[2] == (byte)0xBF) {
            return StandardCharsets.UTF_8;
        }
        return null;
    }

    private void logFileContent(List<String> lines) {
        logger.debug("Lines found: {}", lines.size());
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            logger.debug("Line {}: '{}' (empty: {})", i + 1, line, line.isEmpty());
        }
    }

}