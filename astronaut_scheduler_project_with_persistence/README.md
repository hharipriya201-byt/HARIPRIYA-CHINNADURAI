# Astronaut Daily Schedule Organizer (Console)

A placement-ready console application implementing a daily schedule manager for astronauts.
Focus: design patterns (Singleton, Factory, Observer), SOLID principles, defensive programming, logging and validation.

## Features
- Add / Remove / Edit tasks
- View tasks sorted by start time
- Prevent overlapping tasks (conflict detection)
- Mark tasks completed
- View tasks by priority (Low / Medium / High)
- Notifications of add/update/conflict via Observer pattern
- Logging (using `java.util.logging`)

## Design patterns used
- Singleton: `ScheduleManager`
- Factory: `TaskFactory`
- Observer: `TaskConflictObserver` + `NotificationConsoleObserver`

## Project Structure
```
astronaut-schedule/
 ├─ src/
 │   └─ main/
 │       └─ java/
 │           └─ app/
 │               ├─ Main.java
 │               ├─ ScheduleManager.java
 │               ├─ Task.java
 │               ├─ TaskFactory.java
 │               ├─ TaskConflictObserver.java
 │               ├─ NotificationConsoleObserver.java
 │               ├─ Priority.java
 │               └─ ValidationException.java
 └─ README.md
```

## Build & Run (Java, command-line)
From repo root:

```bash
# compile
javac -d out src/main/java/app/*.java

# run
java -cp out app.Main
```

**Sample session**
```
=== Astronaut Daily Schedule Organizer ===
Choose an option:
1. Add Task
2. Remove Task
3. View All Tasks
4. Edit Task
5. Mark Task Completed/Uncompleted
6. View Tasks by Priority
0. Exit
> 1
Description: Morning Exercise
Start time (HH:mm): 07:00
End time (HH:mm): 08:00
Priority (Low/Medium/High): High
[NOTICE] Task added: 9a4b2c7d: 07:00 - 08:00 [HIGH] Morning Exercise
```

## Key implementation notes
- `TaskFactory` validates input format and ensures `start < end`.
- `ScheduleManager#addTask` checks for overlaps using `Task.overlaps`.
- Observers are notified on add/update/conflict.
- `ValidationException` communicates user-level validation errors.

## What to include in the GitHub link
- Put the project at repository root with `src/` and `README.md`.
- Provide a short commit history and a descriptive initial commit message such as:
  `Initial commit: Astronaut Daily Schedule Organizer — Singleton, Factory, Observer implemented.`

## How to demonstrate during the interview
1. Open `ScheduleManager` to show Singleton implementation and explain thread-safe `getInstance()`.
2. Show `TaskFactory` and explain validation responsibilities (separation of concerns).
3. Show `TaskConflictObserver` and `NotificationConsoleObserver` to explain Observer use for decoupled notifications.
4. Run `Main` and demo adding conflicting task to show conflict detection + observer notification.
5. Explain separation of concerns, SOLID principles, and trade-offs (in-memory store for simplicity; can add persistence later).

## Next steps / Optional
- Add unit tests (JUnit).
- Add persistence (JSON or SQLite) to keep tasks between runs.
- Add more robust logging configuration (file + rotation).
