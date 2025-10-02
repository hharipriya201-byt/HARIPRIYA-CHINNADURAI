# SmartOffice + Design Patterns Demos (Java)

This repository contains two exercises:

1. **Exercise1**: A set of small demos illustrating six design patterns:
   - Behavioural: Command, Interpreter
   - Creational: Singleton, Factory
   - Structural: Composite, Flyweight

   Each demo is self-contained under `exercise1/...` and has a `Main.java` that demonstrates the use case.

2. **Exercise2**: **Smart Office Facility** console application demonstrating:
   - Singleton (OfficeConfig, BookingManager)
   - Observer (Room observers: LightController, ACController, Sensor)
   - Command (BookingCommand, CancelCommand, etc.)
   - Robust logging, validation, defensive programming and scheduled auto-release of bookings.

## How to run

This is a plain Java project (no external build system). To compile and run:

```bash
# From project root
javac -d out $(find . -name "*.java")
java -cp out exercise2.Main
# or run any demo:
java -cp out exercise1.behavioural.command.Main
```

Alternatively, import into your IDE (IntelliJ / Eclipse) as a simple Java project.

## Packaging & GitHub

The project zip is included in this environment. To upload to GitHub:

1. Create a new repo on GitHub.
2. `git init`
3. `git add .`
4. `git commit -m "Initial commit: SmartOffice + pattern demos"`
5. `git remote add origin <your-github-url>`
6. `git push -u origin main`

Only the GitHub link should be shared per instructions.

--- 
Notes:
- No GUI — console/terminal only.
- No hard-coded infinite loops like `while(true) {}`.
- Logging uses `java.util.logging`.
- Auto-release of unoccupied bookings after 5 minutes is implemented using `ScheduledExecutorService` (time accelerated during demo sections for convenience).
