package app;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

public class Task {
    private final String id;
    private String description;
    private LocalTime start;
    private LocalTime end;
    private Priority priority;
    private boolean completed;

    private static final DateTimeFormatter TF = DateTimeFormatter.ofPattern("HH:mm");

    public Task(String description, LocalTime start, LocalTime end, Priority priority) {
        this(UUID.randomUUID().toString(), description, start, end, priority, false);
    }

    // constructor used for loading existing tasks with known id and completed flag
    public Task(String id, String description, LocalTime start, LocalTime end, Priority priority, boolean completed) {
        this.id = id;
        this.description = description;
        this.start = start;
        this.end = end;
        this.priority = priority;
        this.completed = completed;
    }

    public String getId() { return id; }
    public String getDescription() { return description; }
    public LocalTime getStart() { return start; }
    public LocalTime getEnd() { return end; }
    public Priority getPriority() { return priority; }
    public boolean isCompleted() { return completed; }

    public void setDescription(String description) { this.description = description; }
    public void setStart(LocalTime start) { this.start = start; }
    public void setEnd(LocalTime end) { this.end = end; }
    public void setPriority(Priority priority) { this.priority = priority; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    public String timeRange() {
        return start.format(TF) + " - " + end.format(TF);
    }

    @Override
    public String toString() {
        return String.format("%s: %s [%s] %s%s", id.substring(0,8), timeRange(), priority, description, completed ? " (COMPLETED)" : "");
    }

    public boolean overlaps(Task other) {
        return this.start.isBefore(other.end) && other.start.isBefore(this.end);
    }

    public String toCsvLine() {
        // escape double quotes by doubling them
        String descEscaped = description.replace(""", """");
        return String.format("%s,"%s",%s,%s,%s,%b", id, descEscaped, start.format(TF), end.format(TF), priority.name(), completed);
    }

    public static Task fromCsvLine(String line) throws Exception {
        // Expecting: id,"description",start,end,PRIORITY,completed
        // naive CSV parse for this controlled format
        int firstComma = line.indexOf(',');
        String id = line.substring(0, firstComma);
        int firstQuote = line.indexOf('"', firstComma);
        int secondQuote = line.indexOf('"', firstQuote+1);
        while (secondQuote+1 < line.length() && line.charAt(secondQuote+1)== '"') {
            // handle doubled quotes in description
            secondQuote = line.indexOf('"', secondQuote+1);
        }
        String desc = line.substring(firstQuote+1, secondQuote).replace("""", """);
        String rest = line.substring(secondQuote+2); // skip ",
        String[] parts = rest.split(",", -1);
        if (parts.length < 3) throw new Exception("Malformed line: " + line);
        java.time.LocalTime start = java.time.LocalTime.parse(parts[0]);
        java.time.LocalTime end = java.time.LocalTime.parse(parts[1]);
        Priority p = Priority.valueOf(parts[2]);
        boolean completed = false;
        if (parts.length >=4) {
            completed = Boolean.parseBoolean(parts[3]);
        }
        return new Task(id, desc, start, end, p, completed);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task t = (Task) o;
        return Objects.equals(id, t.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
