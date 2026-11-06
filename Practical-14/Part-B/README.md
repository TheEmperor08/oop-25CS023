# MiniBank — final application (Practical 14)

MiniBank is a menu-driven console banking application built step by step across
Practicals 01–14 of CEUE203 Object Oriented Programming. This folder holds the final,
integrated version.

| Layer / package     | Contents |
|---------------------|----------|
| `model`             | `Account` (abstract) + `SavingsAccount`, `CurrentAccount`, `FixedDepositAccount`, `Customer` (nested `Address`), `BankInfo` record, `MenuOption` / `TransactionType` enums, `Command` record, interfaces `Transactable`, `InterestBearing`, `WithdrawRule`, `Premium` |
| `model.annotation`  | `@Id`, `@Positive`, `@MaxLength` |
| `exception`         | `BankException` and its subclasses (insufficient funds, account not found, invalid amount, daily limit) |
| `util`              | `Validator` (regex), `CommandParser`, `StatementFormatter`, `AnnotationValidator` (reflection), generic `Repository<T>`, `StatePersister`, `TransactionLog`, `ReportGenerator` (NIO) |
| `service`           | `BankService` (service layer), `MiniBank` (menu / user interface), `TransactionProcessor` (thread pool + `ConcurrentHashMap`), `AccountStore`, workers and tasks used in the threading labs |

The menu calls `BankService`. `BankService` validates the input, updates the
`Repository<Account>` and either reports success or reports the caught exception.
Accounts are loaded from `data/accounts.dat` at start-up and saved again on exit.
Each transaction is appended to `data/logs/transactions.log`. The report option
writes `data/reports/eod-report.txt`.

## Requirements
- JDK 17 or newer (`java -version`)
- The JUnit 5 console launcher jar, needed only to run the tests:

```
mkdir lib
curl -L -o lib/junit-platform-console-standalone-1.11.4.jar https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.11.4/junit-platform-console-standalone-1.11.4.jar
```

## Build, test and package
Run these from this folder (`Practical-14/Part-B`).

Windows (Command Prompt or PowerShell):
```
.\build.cmd
```

Linux, macOS or GitHub Codespaces:
```
sh build.sh
```

Either script:
1. compiles `src/main/java` into `out/classes`
2. builds the runnable `out/minibank.jar`, using `manifest.txt` with `Main-Class: service.MiniBank`
3. compiles and runs the JUnit tests in `src/test/java`, if the JUnit jar is in `lib/`

### Manual commands (Windows)
```
javac --release 17 -d out\classes src\main\java\exception\*.java src\main\java\model\*.java src\main\java\model\annotation\*.java src\main\java\util\*.java src\main\java\service\*.java
jar cfm out\minibank.jar manifest.txt -C out\classes .
javac --release 17 -d out\test-classes -cp "out\classes;lib\junit-platform-console-standalone-1.11.4.jar" src\test\java\service\*.java
java -jar lib\junit-platform-console-standalone-1.11.4.jar execute --class-path "out\classes;out\test-classes" --scan-class-path --disable-ansi-colors
```
On Linux and macOS, use `/` in paths and `:` as the classpath separator.

## Run
```
java -jar out/minibank.jar
```
To keep the data somewhere else, pass a folder name: `java -jar out/minibank.jar mydata`.

Menu options:
- 1 Open account (savings: minimum balance 1000; current: overdraft 10000; fixed deposit: locked for 1 year)
- 2 Deposit
- 3 Withdraw
- 4 Transfer
- 5 Statement (includes the recent transactions)
- 6 List accounts
- 7 Search by owner
- 8 Batch process: runs on a pool of 4 threads, from a file of lines like `DEPOSIT AC0001 500`, or from a built-in sample
- 9 End-of-day report
- 10 Working hours
- 11 Exit (saves the data)

## Tests (`src/test/java/service/BankServiceTest.java`)
- A deposit increases the balance.
- A withdrawal over the limit throws `InsufficientFundsException` and leaves the balance unchanged.
- Invalid amounts and unknown account numbers are rejected.
- A failed transfer changes nothing.
- Many concurrent deposits, and a concurrent batch on the thread pool, keep the balance exact.
- State saved to a file loads back unchanged.
- Search by owner ignores case.

Generated files (`out/`, `data/`, `lib/*.jar`) are not committed.
