package app;

public interface TaskConflictObserver {
    void onTaskAdded(Task task);
    void onTaskConflict(Task newTask, Task conflictingTask);
    void onTaskRemoved(Task task);
    void onTaskUpdated(Task task);
}
