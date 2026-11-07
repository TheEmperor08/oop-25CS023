# MiniBank — Practical 01 (menu shell)

Console menu shell for MiniBank: a `BankInfo` record header, a `MenuOption` enum,
a `Scanner`-driven numbered menu and a switch expression that prints a placeholder
for each option until **EXIT** is chosen.

## Requirements
- JDK 17 or newer (`java -version`)

## Compile
From this folder (`MiniProject/01-Practical-01`):

```
javac -d out src/*.java
```

## Run
```
java -cp out MiniBank
```

Enter a menu number (for example `2` for Deposit). Invalid input is rejected and the
user is asked again. Choose `6` (EXIT) to quit. The `out/` folder holds generated
`.class` files and is not committed.
