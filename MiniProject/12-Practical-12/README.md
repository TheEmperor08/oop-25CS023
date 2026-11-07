# MiniBank - Practical 12

AccountStore (HashMap + ArrayList history), Comparable/Comparator sorting, ConcurrentHashMap in TransactionProcessor. Milestone 2.

This folder is the cumulative MiniBank checkpoint after Practical 12 (it builds on Practical 11). `MiniBank.main` runs this practical's demo, then shows the menu from Practical 01. Choose `6` (EXIT) to quit.

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
