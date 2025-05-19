@echo off
echo Employee Management System
echo =========================
echo.

rem Check if Java is installed
java -version >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo Error: Java is not installed or not in the PATH
    echo Please install Java or add it to your system PATH
    pause
    exit /b 1
)

echo Java is installed. Proceeding with compilation...
echo.

rem Create bin directory if it doesn't exist
if not exist bin mkdir bin

rem Clean bin directory
echo Cleaning previous build...
del /Q bin\*.class 2>nul

echo Compiling Employee Management System...
javac -d bin src\*.java
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo Compilation failed!
    echo Please check the error messages above.
    pause
    exit /b 1
)

echo.
echo Compilation successful!
echo.

rem Check if data directory and file exist
if not exist data (
    echo Creating data directory...
    mkdir data
)

if not exist data\employees.txt (
    echo Creating empty employees.txt file...
    type nul > data\employees.txt
)

if not exist data\attendance.txt (
    echo Creating empty attendance.txt file...
    type nul > data\attendance.txt
)

echo Running Employee Management System...
echo.
java -cp bin Main
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo Application terminated with an error!
    echo Please check the error messages above.
)
pause
