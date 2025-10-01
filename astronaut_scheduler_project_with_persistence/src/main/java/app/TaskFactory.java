package app;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.logging.Logger;

public class TaskFactory {
    private static final Logger logger = Logger.getLogger(TaskFactory.class.getName());
    private static final DateTimeFormatter TF = DateTimeFormatter.ofPattern("HH:mm");

    public static Task createTask(String description, String startStr, String endStr, String priorityStr) throws ValidationException {
        if (description == null || description.trim().isEmpty()) {
            throw new ValidationException("Description cannot be empty.");
        }

        LocalTime start, end;
        try {
            start = LocalTime.parse(startStr.trim(), TF);
            end = LocalTime.parse(endStr.trim(), TF);
        } catch (DateTimeParseException e) {
            throw new ValidationException("Invalid time format. Use HH:mm (24-hour).");
        }

        if (!start.isBefore(end)) {
            throw new ValidationException("Start time must be before end time.");
        }

        Priority p = Priority.fromString(priorityStr);
        if (p == null) {
            throw new ValidationException("Invalid priority. Use Low, Medium, or High.");
        }

        logger.fine(String.format("Creating Task: %s %s-%s %s", description, startStr, endStr, p));
        return new Task(description.trim(), start, end, p);
    }
}
