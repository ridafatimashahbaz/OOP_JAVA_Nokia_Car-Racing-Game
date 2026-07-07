@echo off
echo ============================================
echo   Nokia Racing Car - OOP Java Project
echo   Build and Run Script (Windows)
echo ============================================
echo.

:: Create output directory
if not exist "out" mkdir out

:: Compile all Java files
echo [1/2] Compiling...
javac -d out -sourcepath src src/game/Main.java

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERROR: Compilation failed. Check your Java installation.
    echo Make sure JDK is installed and 'javac' is in your PATH.
    pause
    exit /b 1
)

echo [2/2] Compilation successful!
echo.
echo Starting Racing Car...
echo.

:: Run the game
java -cp out game.Main

pause
