package app;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ScheduleManager {
    private static final Logger logger = Logger.getLogger(ScheduleManager.class.getName());
    private static ScheduleManager instance;

    private final List<Task> tasks;
    private final List<TaskConflictObserver> observers;
    private final PersistenceManager persistence;
    private final String persistenceFile = "tasks_data.csv";

    private ScheduleManager() {
        this.tasks = new ArrayList<>();
        this.observers = new ArrayList<>();
        this.persistence = new PersistenceManager(persistenceFile);
        // load existing tasks
        List<Task> loaded = persistence.loadAll();
        if (!loaded.isEmpty()) {
            tasks.addAll(loaded);
            tasks.sort(Comparator.comparing(Task::getStart));
            logger.info("Loaded " + loaded.size() + " tasks from persistence.");
        }
    }

    public static synchronized ScheduleManager getInstance() {
        if (instance == null) {
            instance = new ScheduleManager();
            logger.info("ScheduleManager singleton created.");
        }
        return instance;
    }

    public void registerObserver(TaskConflictObserver o) {
        observers.add(o);
    }

    public void unregisterObserver(TaskConflictObserver o) {
        observers.remove(o);
    }

    private void notifyAdded(Task t) { observers.forEach(o -> {
        try { o.onTaskAdded(t); } catch (Exception e) { logger.log(Level.WARNING, "Observer failed onAdded", e); }
    }); }
    private void notifyConflict(Task newTask, Task conflicting) { observers.forEach(o -> {
        try { o.onTaskConflict(newTask, conflicting);} catch (Exception e) { logger.log(Level.WARNING, "Observer failed onConflict", e); }
    }); }
    private void notifyRemoved(Task t) { observers.forEach(o -> {
        try { o.onTaskRemoved(t);} catch (Exception e) { logger.log(Level.WARNING, "Observer failed onRemoved", e); }
    }); }
    private void notifyUpdated(Task t) { observers.forEach(o -> {
        try { o.onTaskUpdated(t);} catch (Exception e) { logger.log(Level.WARNING, "Observer failed onUpdated", e); }
    }); }

    public synchronized void addTask(Task t) throws ValidationException {
        for (Task existing : tasks) {
            if (t.overlaps(existing)) {
                notifyConflict(t, existing);
                throw new ValidationException("Task conflicts with existing task: " + existing.getDescription());
            }
        }
        tasks.add(t);
        tasks.sort(Comparator.comparing(Task::getStart));
        logger.info("Task added: " + t);
        notifyAdded(t);
    }

    public synchronized void removeTaskById(String id) throws ValidationException {
        Optional<Task> opt = tasks.stream().filter(x -> x.getId().startsWith(id) || x.getId().equals(id)).findFirst();
        if (opt.isEmpty()) throw new ValidationException("Task not found for id: " + id);
        Task t = opt.get();
        tasks.remove(t);
        notifyRemoved(t);
        logger.info("Task removed: " + t);
    }

    public synchronized void editTaskById(String id, String desc, String startStr, String endStr, Priority priority) throws ValidationException {
        Optional<Task> opt = tasks.stream().filter(x -> x.getId().startsWith(id) || x.getId().equals(id)).findFirst();
        if (opt.isEmpty()) throw new ValidationException("Task not found for id: " + id);
        Task t = opt.get();

        Task temp = new Task(desc == null ? t.getDescription() : desc,
                startStr == null ? t.getStart() : java.time.LocalTime.parse(startStr),
                endStr == null ? t.getEnd() : java.time.LocalTime.parse(endStr),
                priority == null ? t.getPriority() : priority);

        for (Task other : tasks) {
            if (other.equals(t)) continue;
            if (temp.overlaps(other)) {
                notifyConflict(temp, other);
                throw new ValidationException("Updated task would conflict with: " + other.getDescription());
            }
        }

        if (desc != null) t.setDescription(desc);
        if (startStr != null) t.setStart(java.time.LocalTime.parse(startStr));
        if (endStr != null) t.setEnd(java.time.LocalTime.parse(endStr));
        if (priority != null) t.setPriority(priority);
        tasks.sort(Comparator.comparing(Task::getStart));
        notifyUpdated(t);
        logger.info("Task updated: " + t);
    }

    public synchronized void markCompleted(String id, boolean completed) throws ValidationException {
        Optional<Task> opt = tasks.stream().filter(x -> x.getId().startsWith(id) || x.getId().equals(id)).findFirst();
        if (opt.isEmpty()) throw new ValidationException("Task not found for id: " + id);
        Task t = opt.get();
        t.setCompleted(completed);
        notifyUpdated(t);
        logger.info("Task marked completed=" + completed + " : " + t);
    }

    public synchronized List<Task> getAllTasks() {
        return tasks.stream().sorted(Comparator.comparing(Task::getStart)).collect(Collectors.toList());
    }

    public synchronized List<Task> getTasksByPriority(Priority p) {
        return tasks.stream().filter(t -> t.getPriority() == p).sorted(Comparator.comparing(Task::getStart)).collect(Collectors.toList());
    }

    public synchronized boolean saveToPersistence() {
        try {
            boolean ok = persistence.saveAll(tasks);
            if (!ok) logger.warning("Failed to save tasks to persistence after retries.");
            return ok;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error while saving tasks", e);
            return false;
        }
    }
}
