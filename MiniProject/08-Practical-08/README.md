# MiniBank - Practical 08

Checked exception hierarchy (BankException ...), throw/throws in deposit/withdraw/transfer, try-catch-finally, try-with-resources. Milestone 1.

This folder is the cumulative MiniBank checkpoint after Practical 08 (it builds on Practical 07). `MiniBank.main` runs this practical's demo, then shows the menu from Practical 01. Choose `6` (EXIT) to quit.

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
