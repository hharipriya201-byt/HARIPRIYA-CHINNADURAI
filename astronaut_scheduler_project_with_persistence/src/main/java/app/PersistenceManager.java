package app;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class PersistenceManager {
    private static final Logger logger = Logger.getLogger(PersistenceManager.class.getName());
    private final Path filePath;
    private final int maxRetries = 3;
    private final long retryDelayMillis = 200;

    public PersistenceManager(String filename) {
        this.filePath = Paths.get(filename);
    }

    public List<Task> loadAll() {
        List<Task> out = new ArrayList<>();
        int attempt = 0;
        while (attempt < maxRetries) {
            try {
                if (!Files.exists(filePath)) return out;
                List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
                for (String line : lines) {
                    if (line == null || line.trim().isEmpty()) continue;
                    try {
                        Task t = Task.fromCsvLine(line);
                        out.add(t);
                    } catch (Exception ex) {
                        logger.warning("Skipping malformed line in persistence file: " + line);
                    }
                }
                return out;
            } catch (IOException e) {
                attempt++;
                logger.warning("Failed to load tasks (attempt " + attempt + "): " + e.getMessage());
                try { Thread.sleep(retryDelayMillis); } catch (InterruptedException ignored) {}
            }
        }
        return out;
    }

    public boolean saveAll(List<Task> tasks) {
        int attempt = 0;
        StringBuilder sb = new StringBuilder();
        for (Task t : tasks) {
            sb.append(t.toCsvLine()).append(System.lineSeparator());
        }
        byte[] bytes = sb.toString().getBytes(StandardCharsets.UTF_8);
        while (attempt < maxRetries) {
            try {
                Files.createDirectories(filePath.getParent() == null ? Paths.get(".") : filePath.getParent());
                Files.write(filePath, bytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
                return true;
            } catch (IOException e) {
                attempt++;
                logger.warning("Failed to save tasks (attempt " + attempt + "): " + e.getMessage());
                try { Thread.sleep(retryDelayMillis); } catch (InterruptedException ignored) {}
            }
        }
        return false;
    }
}
