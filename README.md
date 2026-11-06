# OOP CEUE203 — Practicals 01–14

**Student:** Shreyas (25CS023)
**Course:** CEUE203 — Object Oriented Programming, A.Y. 2026–27, Semester 3
**Java:** JDK 17 or newer

This repository holds the Java source for every practical in the official practical list:
- **Part A** has the practice programs, including the "For Advanced Learners" programs.
- **Part B** is **MiniBank**, a console banking application built up cumulatively across the semester.

## Layout

```
Practical-NN/
  Part-A/KK-ProgramName/   one folder per practice program (each compiles on its own)
  Part-B/                  MiniBank checkpoint after Practical NN (README.md: how to build and run)
```

Each `Practical-NN/Part-B` folder is a complete, compilable snapshot of MiniBank at the end
of that practical, and each one builds on the previous one. Git tags `lab-01` … `lab-14`
mark the same checkpoints in the history.

| Practical | MiniBank milestone |
|---|---|
| 01 | Menu shell: `BankInfo` record, `MenuOption` enum, switch expressions, `Scanner` |
| 02 | `Customer` and `Account` classes, encapsulation, constructor chaining, `Account[]` |
| 03 | `toString` / `equals` / `hashCode`, nested `Customer.Address`, `clone()`, `instanceof` |
| 04 | `Validator` (regex), `Command` record, `CommandParser`, `StatementFormatter` |
| 05 | Abstract `Account` with Savings / Current / Fixed-deposit subclasses, polymorphism |
| 06 | Interfaces, packages `model` / `util` / `service`, static import, runnable JAR |
| 07 | `@Override`, custom annotations `@Id` / `@Positive` / `@MaxLength`, reflection validator |
| 08 | Custom checked exception hierarchy, try-catch-finally, try-with-resources (Milestone 1) |
| 09 | Threads, race condition, `synchronized` deposit / withdraw |
| 10 | Thread pool `TransactionProcessor`, producer–consumer, deadlock and lock ordering |
| 11 | Serialization, append-only transaction log, NIO end-of-day report |
| 12 | Collections: `AccountStore`, `Comparable` / `Comparator`, `ConcurrentHashMap` (Milestone 2) |
| 13 | Generic `Repository<T>` keyed by `@Id` through reflection |
| 14 | Final application: `BankService`, real menu, persistence, batch, report, JUnit tests, JAR (Milestone 3) |

## Quick start (final application)

```
cd Practical-14/Part-B
.\build.cmd          (Windows)     or     sh build.sh     (Linux / macOS / Codespaces)
java -jar out/minibank.jar
```

See `Practical-14/Part-B/README.md` for the JUnit setup, the manual build commands and the
menu options.

## Part A programs
Compile and run any program from its own folder, for example:

```
cd Practical-01/Part-A/01-VendingMachine
javac -d out *.java
java -cp out VendingMachine
```

Build output (`out/`, `*.class`, `*.jar`) and runtime data (`data/`) are excluded by `.gitignore`.
