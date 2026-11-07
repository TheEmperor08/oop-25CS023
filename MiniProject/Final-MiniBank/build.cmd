@echo off
rem Build, test and package MiniBank on Windows (JDK 17 or newer).
rem   OUT   - output folder (default: out)
rem   JUNIT - JUnit 5 console launcher jar (default: lib\junit-platform-console-standalone-1.11.4.jar)
setlocal
if not defined OUT set OUT=out
if not defined JUNIT set JUNIT=lib\junit-platform-console-standalone-1.11.4.jar
if exist "%OUT%" rmdir /s /q "%OUT%"
set SRC=src\main\java
javac --release 17 -d "%OUT%\classes" %SRC%\exception\*.java %SRC%\model\*.java %SRC%\model\annotation\*.java %SRC%\util\*.java %SRC%\service\*.java
if errorlevel 1 exit /b 1
jar cfm "%OUT%\minibank.jar" manifest.txt -C "%OUT%\classes" .
if errorlevel 1 exit /b 1
echo Built %OUT%\minibank.jar
if not exist "%JUNIT%" (
    echo JUnit jar not found at %JUNIT% - tests skipped. See README.md.
    exit /b 0
)
javac --release 17 -d "%OUT%\test-classes" -cp "%OUT%\classes;%JUNIT%" src\test\java\service\*.java
if errorlevel 1 exit /b 1
java -jar "%JUNIT%" execute --class-path "%OUT%\classes;%OUT%\test-classes" --scan-class-path --disable-ansi-colors --details=tree --disable-banner
