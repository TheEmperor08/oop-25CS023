# MiniBank - Practical 13

Generic Repository<T> keyed by the @Id field via reflection, @SafeVarargs saveAll, bounded generic average().

This folder is the cumulative MiniBank checkpoint after Practical 13 (it builds on Practical 12). `MiniBank.main` runs this practical's demo, then shows the menu from Practical 01. Choose `6` (EXIT) to quit.

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
