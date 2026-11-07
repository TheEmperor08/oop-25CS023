# MiniBank - Practical 06

Interfaces (Transactable, InterestBearing, WithdrawRule, Premium), packages model/util/service, static import, runnable JAR.

This folder is the cumulative MiniBank checkpoint after Practical 06 (it builds on Practical 05). `MiniBank.main` runs this practical's demo, then shows the menu from Practical 01. Choose `6` (EXIT) to quit.

## Compile and run (JDK 17+), from this folder

Windows:
```
javac -d out src\model\*.java src\util\*.java src\service\*.java
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
