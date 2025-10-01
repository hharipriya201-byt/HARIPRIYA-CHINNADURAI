package app;

import java.util.logging.Logger;

public class NotificationConsoleObserver implements TaskConflictObserver {
    private static final Logger logger = Logger.getLogger(NotificationConsoleObserver.class.getName());

    @Override
    public void onTaskAdded(Task task) {
        System.out.println("[NOTICE] Task added: " + task);
    }

    @Override
    public void onTaskConflict(Task newTask, Task conflictingTask) {
        System.out.println("[CONFLICT] New task '" + newTask.getDescription() + "' conflicts with existing '" + conflictingTask.getDescription() + "' (" + conflictingTask.timeRange() + ")");
    }

    @Override
    public void onTaskRemoved(Task task) {
        System.out.println("[NOTICE] Task removed: " + task);
    }

    @Override
    public void onTaskUpdated(Task task) {
        System.out.println("[NOTICE] Task updated: " + task);
    }
}
