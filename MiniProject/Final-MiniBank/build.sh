#!/bin/sh
# Build, test and package MiniBank on Linux/macOS/Codespaces (JDK 17 or newer).
#   OUT   - output folder (default: out)
#   JUNIT - JUnit 5 console launcher jar (default: lib/junit-platform-console-standalone-1.11.4.jar)
set -e
OUT="${OUT:-out}"
JUNIT="${JUNIT:-lib/junit-platform-console-standalone-1.11.4.jar}"
rm -rf "$OUT"
javac --release 17 -d "$OUT/classes" $(find src/main/java -name '*.java')
jar cfm "$OUT/minibank.jar" manifest.txt -C "$OUT/classes" .
echo "Built $OUT/minibank.jar"
if [ ! -f "$JUNIT" ]; then
    echo "JUnit jar not found at $JUNIT - tests skipped. See README.md."
    exit 0
fi
javac --release 17 -d "$OUT/test-classes" -cp "$OUT/classes:$JUNIT" $(find src/test/java -name '*.java')
java -jar "$JUNIT" execute --class-path "$OUT/classes:$OUT/test-classes" --scan-class-path --disable-ansi-colors --details=tree --disable-banner
