# MiniBank — Final Integrated Application

This folder holds the final, integrated MiniBank application: the result of the Part B
project work across Practicals 01–14 of CEUE203 Object Oriented Programming. It is the
same code as `14-Practical-14` and builds and runs on its own. Nothing outside this folder
is needed except a JDK (and, for the tests only, the JUnit jar described below).

**Student:** Shreyas (25CS023) · CEUE203 – Object Oriented Programming · A.Y. 2026–27, Semester 3

## Folder contents

```
Final-MiniBank/
  src/main/java/
    exception/          BankException and its subclasses
    model/              Account (abstract), SavingsAccount, CurrentAccount, FixedDepositAccount,
                        Customer, BankInfo, MenuOption, TransactionType, Command, interfaces
    model/annotation/   @Id, @Positive, @MaxLength
    util/               Validator, CommandParser, StatementFormatter, AnnotationValidator,
                        Repository<T>, StatePersister, TransactionLog, ReportGenerator
    service/            MiniBank (menu), BankService (service layer), TransactionProcessor,
                        AccountStore, workers and tasks from the threading practicals
  src/test/java/service/BankServiceTest.java   JUnit 5 tests
  manifest.txt          Main-Class: service.MiniBank (used to build the runnable JAR)
  build.cmd             Windows build + package + test script
  build.sh              Linux / macOS / Codespaces version of the same script
```

The menu (`service.MiniBank`) only talks to `BankService`. `BankService` validates the
input, updates the generic `Repository<Account>`, writes every transaction to the log and
reports errors through the checked `BankException` hierarchy.

## Requirements
- JDK 17 or newer. Check with `java -version` and `javac -version`.
- Only for the tests: the JUnit 5 console launcher jar in a `lib` folder here.

## Windows: build, test and run

Open **Command Prompt** or **PowerShell** in this folder (`MiniProject\Final-MiniBank`).

1. *(Optional, for the tests)* download the JUnit jar once:
   ```
   mkdir lib
   curl -L -o lib\junit-platform-console-standalone-1.11.4.jar https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.11.4/junit-platform-console-standalone-1.11.4.jar
   ```
2. Build, package and test:
   ```
   .\build.cmd
   ```
   The script compiles `src\main\java` into `out\classes`, builds the runnable
   `out\minibank.jar` and, if the JUnit jar is present, compiles and runs the tests in
   `src\test\java` (9 tests). Without the jar it builds the program and skips the tests.
3. Run the application:
   ```
   java -jar out\minibank.jar
   ```
   To keep the data in another folder, pass its name: `java -jar out\minibank.jar mydata`.

### Manual commands (Windows, without the script)
```
javac --release 17 -d out\classes src\main\java\exception\*.java src\main\java\model\*.java src\main\java\model\annotation\*.java src\main\java\util\*.java src\main\java\service\*.java
jar cfm out\minibank.jar manifest.txt -C out\classes .
java -jar out\minibank.jar
```
Tests:
```
javac --release 17 -d out\test-classes -cp "out\classes;lib\junit-platform-console-standalone-1.11.4.jar" src\test\java\service\*.java
java -jar lib\junit-platform-console-standalone-1.11.4.jar execute --class-path "out\classes;out\test-classes" --scan-class-path --disable-ansi-colors
```

## Linux / macOS / GitHub Codespaces
```
sh build.sh
java -jar out/minibank.jar
```
(Download the JUnit jar into `lib/` first, as above, to run the tests.)

## Using the application

Menu options:

| No. | Option | What it does |
|---|---|---|
| 1 | Open account | Savings (minimum balance 1000), current (overdraft 10000) or fixed deposit (locked for 1 year) |
| 2 | Deposit | Adds money to an account |
| 3 | Withdraw | Withdraws money; refused if it breaks the account's rules |
| 4 | Transfer | Moves money between two accounts; rolled back if it fails |
| 5 | Statement | Account details plus its recent transactions |
| 6 | List accounts | All accounts, sorted by account number |
| 7 | Search by owner | Accounts whose owner name contains the text (case ignored) |
| 8 | Batch process | Runs commands such as `DEPOSIT AC0001 500` from a file (or a built-in sample) on a pool of 4 threads |
| 9 | Report | Writes the end-of-day report |
| 10 | Working hours | Prints the bank's working hours |
| 11 | Exit | Saves all accounts and quits |

Data files (created in `data\` next to where you run the program):
- `data\accounts.dat`: the saved accounts, loaded again at the next start
- `data\logs\transactions.log`: every transaction, appended
- `data\reports\eod-report.txt`: the end-of-day report

## Tests (`src/test/java/service/BankServiceTest.java`)
- A deposit increases the balance.
- A withdrawal over the limit throws `InsufficientFundsException` and leaves the balance unchanged.
- Zero, negative and non-numeric amounts are rejected; an unknown account number throws `AccountNotFoundException`.
- A transfer moves money; a failed transfer changes nothing.
- Many concurrent deposits, and a concurrent batch on the thread pool, keep the balance exact.
- State saved to a file loads back unchanged.
- Search by owner ignores case.

Generated files (`out\`, `data\`, `lib\*.jar`) are not committed.
