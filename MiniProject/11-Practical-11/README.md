# MiniBank - Practical 11

Serializable model, StatePersister (save/load), append-only TransactionLog, NIO ReportGenerator.

This folder is the cumulative MiniBank checkpoint after Practical 11 (it builds on Practical 10). `MiniBank.main` runs this practical's demo, then shows the menu from Practical 01. Choose `6` (EXIT) to quit.

The program creates a `data/` folder (accounts.dat, logs, reports) in the working directory. Run it twice to see the saved accounts loaded again. Generated data is not committed.

## Compile and run (JDK 17+), from this folder

Windows:
```
javac -d out src\exception\*.java src\model\*.java src\model\annotation\*.java src\util\*.java src\service\*.java
jar cfm minibank.jar manifest.txt -C out .
java -jar minibank.jar
```

Linux / macOS / Codespaces:
```
javac -d out $(find src -name '*.java')
jar cfm minibank.jar manifest.txt -C out .
java -jar minibank.jar
```

`out/` and `*.jar` are build output and are not committed.
