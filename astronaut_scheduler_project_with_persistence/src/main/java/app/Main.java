package app;

import java.util.List;
import java.util.Scanner;
import java.util.logging.*;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static final ScheduleManager manager = ScheduleManager.getInstance();

    static {
        // configure simple console logger
        Logger root = Logger.getLogger("");
        Handler[] handlers = root.getHandlers();
        for (Handler h : handlers) h.setLevel(Level.INFO);
        root.setLevel(Level.INFO);
    }

    public static void main(String[] args) {
        manager.registerObserver(new NotificationConsoleObserver());
        // add shutdown hook to persist tasks
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\n[Saving] Persisting tasks before exit..."); 
            boolean ok = manager.saveToPersistence();
            if (ok) System.out.println("[Saved] Tasks persisted successfully.");
            else System.out.println("[Warning] Failed to persist tasks.");
        }));

        System.out.println("=== Astronaut Daily Schedule Organizer ===");
        try (Scanner sc = new Scanner(System.in)) {
            boolean exit = false;
            while (!exit) {
                printMenu();
                String choice = sc.nextLine().trim();
                switch (choice) {
                    case "1": handleAdd(sc); break;
                    case "2": handleRemove(sc); break;
                    case "3": handleViewAll(); break;
                    case "4": handleEdit(sc); break;
                    case "5": handleMarkCompleted(sc); break;
                    case "6": handleViewByPriority(sc); break;
                    case "0": exit = true; break;
                    default: System.out.println("Unknown option. Try again."); break;
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unhandled exception", e);
        }
        System.out.println("Goodbye! (press Enter to finish)"); 
        // allow shutdown hook to run after user presses enter
        try { System.in.read(); } catch (Exception ignored) {}
    }

    private static void printMenu() {
        System.out.println("\nChoose an option:");
        System.out.println("1. Add Task");
        System.out.println("2. Remove Task");
        System.out.println("3. View All Tasks");
        System.out.println("4. Edit Task");
        System.out.println("5. Mark Task Completed/Uncompleted");
        System.out.println("6. View Tasks by Priority");
        System.out.println("0. Exit");
        System.out.print("> ");
    }

    private static void handleAdd(Scanner sc) {
        try {
            System.out.print("Description: ");
            String desc = sc.nextLine();
            System.out.print("Start time (HH:mm): ");
            String start = sc.nextLine();
            System.out.print("End time (HH:mm): ");
            String end = sc.nextLine();
            System.out.print("Priority (Low/Medium/High): ");
            String pr = sc.nextLine();

            Task t = TaskFactory.createTask(desc, start, end, pr);
            manager.addTask(t);
        } catch (ValidationException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }

    private static void handleRemove(Scanner sc) {
        try {
            System.out.print("Enter Task ID (short id allowed): ");
            String id = sc.nextLine();
            manager.removeTaskById(id);
        } catch (ValidationException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void handleViewAll() {
        List<Task> tasks = manager.getAllTasks();
        if (tasks.isEmpty()) {
            System.out.println("No tasks scheduled.");
            return;
        }
        tasks.forEach(System.out::println);
    }

    private static void handleEdit(Scanner sc) {
        try {
            System.out.print("Enter Task ID to edit: ");
            String id = sc.nextLine();
            System.out.print("New description (or blank to keep): ");
            String desc = sc.nextLine();
            desc = desc.isBlank() ? null : desc;
            System.out.print("New start time (HH:mm) or blank: ");
            String start = sc.nextLine();
            start = start.isBlank() ? null : start;
            System.out.print("New end time (HH:mm) or blank: ");
            String end = sc.nextLine();
            end = end.isBlank() ? null : end;
            System.out.print("New priority (Low/Medium/High) or blank: ");
            String pr = sc.nextLine();
            Priority p = pr.isBlank() ? null : Priority.fromString(pr);

            manager.editTaskById(id, desc, start, end, p);
        } catch (ValidationException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void handleMarkCompleted(Scanner sc) {
        try {
            System.out.print("Enter Task ID: ");
            String id = sc.nextLine();
            System.out.print("Mark as completed? (y/n): ");
            String yn = sc.nextLine().trim().toLowerCase();
            manager.markCompleted(id, yn.startsWith("y"));
        } catch (ValidationException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void handleViewByPriority(Scanner sc) {
        System.out.print("Enter priority (Low/Medium/High): ");
        String pr = sc.nextLine();
        Priority p = Priority.fromString(pr);
        if (p == null) {
            System.out.println("Invalid priority.");
            return;
        }
        List<Task> tasks = manager.getTasksByPriority(p);
        if (tasks.isEmpty()) {
            System.out.println("No tasks with priority " + p);
            return;
        }
        tasks.forEach(System.out::println);
    }
}
