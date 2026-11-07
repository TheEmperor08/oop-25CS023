# MiniBank - Practical 02

Customer and Account classes: encapsulation, constructors with this(), static id counters, Account[] array.

This folder is the cumulative MiniBank checkpoint after Practical 02 (it builds on Practical 01). `MiniBank.main` runs this practical's demo, then shows the menu from Practical 01. Choose `6` (EXIT) to quit.

`check/EncapsulationCheck.java` is a post-lab check that is expected NOT to compile: it shows the private balance cannot be changed from outside `Account`.

## Compile and run (JDK 17+), from this folder

```
javac -d out src/*.java
java -cp out MiniBank
```

`out/` and `*.jar` are build output and are not committed.
