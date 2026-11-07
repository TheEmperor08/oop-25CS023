# Mini Project — MiniBank

| | |
|---|---|
| **Name** | Shreyas |
| **Student ID** | 25CS023 |
| **Course** | CEUE203 – Object Oriented Programming |
| **Academic Year** | 2026–27 |
| **Semester** | 3 |

## Overview

MiniBank is a menu-driven console banking application written in Java. It is the Part B
project work of CEUE203 and was developed incrementally across 14 practical milestones:
each practical added the object-oriented topic of that week to the same code base. The
final version is a layered, tested and packaged application. Users can open accounts,
deposit, withdraw and transfer money, view statements, process batches of transactions
concurrently and produce an end-of-day report. All data is saved between runs.

### Objectives
- Apply every major OOP concept of the course (encapsulation, inheritance, polymorphism,
  abstraction, interfaces, annotations, exceptions, generics) in one growing program.
- Make the program safe under concurrency (synchronization, thread pools, deadlock avoidance).
- Persist data with serialization and NIO, and store it with the Collections Framework.
- Finish with a layered architecture, unit tests and a runnable JAR.

## Folder structure

```
MiniProject/
  01-Practical-01/ … 14-Practical-14/   MiniBank as it stood at the end of each practical
  Final-MiniBank/                        the final integrated application (independently buildable)
  README.md                              this file
```

Each numbered folder is a complete, compilable snapshot of the Part B MiniBank source at
the end of that practical, with its own `README.md` explaining how to compile and run it.
Each snapshot builds on the previous one. `Final-MiniBank` contains the same code as
`14-Practical-14`, with its own README covering the build, the tests and how to use the app.

| Folder layout | Practicals |
|---|---|
| `src/*.java` (default package) | 01–05 |
| `src/{exception,model,model/annotation,util,service}` + `manifest.txt` | 06–13 |
| `src/main/java`, `src/test/java`, `manifest.txt`, `build.cmd`, `build.sh` | 14 and Final-MiniBank |

## Milestones (chronological)

| # | Milestone | What was added |
|---|---|---|
| 01 | Menu shell | `MiniBank` main class, `BankInfo` record, `MenuOption` enum, `Scanner` input, switch expressions; invalid input re-prompted; working-hours option |
| 02 | Customer and Account classes | Encapsulated `Customer` and `Account`, generated IDs (`CUST101`, `AC0001`), constructor chaining, validated `deposit`/`withdraw`, `Account[]`; post-lab check that `balance` cannot be changed from outside |
| 03 | Object methods | `toString`, `equals`/`hashCode` by account number, static nested `Customer.Address`, `clone()`, `instanceof`, formatted statement |
| 04 | Validation and parsing | Regex `Validator` (mobile, email, PAN, IFSC, amount), `TransactionType` enum, `Command` record, `CommandParser`, `StatementFormatter` (StringBuilder) |
| 05 | Account types | Abstract `Account`; `SavingsAccount` (minimum balance, 4%), `CurrentAccount` (overdraft, 0%), `FixedDepositAccount` (7%, locked until maturity); run-time polymorphism and pattern `instanceof` |
| 06 | Interfaces and packages | `Transactable`, `InterestBearing` (default, static and private methods), functional `WithdrawRule`, marker `Premium`; packages `model`/`util`/`service`; static import; runnable JAR |
| 07 | Annotations and reflection | `@Override`, `@FunctionalInterface`; custom `@Id`, `@Positive`, `@MaxLength`; `AnnotationValidator` using reflection |
| 08 | Exception handling (Milestone 1) | Checked `BankException` hierarchy (insufficient funds, account not found, invalid amount, daily limit), try-catch-finally transfer rollback, try-with-resources `BankSession` |
| 09 | Multithreading | `AccountWorker`/`WithdrawWorker` threads, race condition shown and fixed with `synchronized`, `AtomicLong` comparison |
| 10 | Transaction engine | `TransactionProcessor` (fixed thread pool), producer–consumer `TransactionBuffer` (wait/notifyAll), deadlock detection (`ThreadMXBean`) and prevention by lock ordering |
| 11 | Persistence and reporting | `Serializable` model with `transient` fields, `StatePersister`, append-only `TransactionLog`, NIO `ReportGenerator` (DirectoryStream, file attributes) |
| 12 | Collections (Milestone 2) | `AccountStore` (HashMap, ArrayList history, TreeMap), `Comparable`/`Comparator` sorting, `ConcurrentHashMap` in the transaction processor |
| 13 | Generics | Generic `Repository<T>` keyed by the `@Id` field via reflection, `@SafeVarargs`, bounded `<N extends Number>` average |
| 14 | Final integration (Milestone 3) | `BankService` service layer; full 11-option menu; persistence across runs; concurrent batch processing; statement, search and end-of-day report; JUnit 5 tests; build scripts and runnable JAR |

## Technologies
- Java 17 (records, enums, switch expressions, pattern matching for `instanceof`)
- Java standard library: Collections Framework, `java.util.concurrent`, `java.nio.file`,
  object serialization, reflection, `java.util.regex`
- JUnit 5 (console launcher 1.11.4) for the unit tests
- `javac` / `jar` command-line tools; `build.cmd` (Windows) and `build.sh` (Linux/macOS)

## Build and run the final application (Windows)

From `MiniProject\Final-MiniBank`:
```
.\build.cmd
java -jar out\minibank.jar
```
On Linux, macOS or Codespaces: `sh build.sh`, then `java -jar out/minibank.jar`.

## Run the tests

The tests need the JUnit 5 console launcher jar in `Final-MiniBank\lib` (not committed):
```
cd MiniProject\Final-MiniBank
mkdir lib
curl -L -o lib\junit-platform-console-standalone-1.11.4.jar https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.11.4/junit-platform-console-standalone-1.11.4.jar
.\build.cmd
```
`build.cmd` runs all 9 tests after building the JAR. See `Final-MiniBank/README.md` for the
manual commands.

## Running an earlier milestone
Open its folder and follow its README. For example:
```
cd MiniProject\05-Practical-05
javac -d out src\*.java
java -cp out MiniBank
```
For Practicals 06–13, compile all packages and run `service.MiniBank` (or build the JAR
from `manifest.txt`), as described in each folder's README.

Build output (`out/`, `*.class`, `*.jar`) and runtime data (`data/`) are not committed.
