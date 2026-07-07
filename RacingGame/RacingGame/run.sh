#!/bin/bash
echo "============================================"
echo "  Nokia Racing Car - OOP Java Project"
echo "  Build and Run Script (Linux/Mac)"
echo "============================================"
echo ""

# Create output directory
mkdir -p out

# Compile all Java files
echo "[1/2] Compiling..."
find src -name "*.java" | xargs javac -d out -sourcepath src

if [ $? -ne 0 ]; then
    echo ""
    echo "ERROR: Compilation failed."
    echo "Make sure JDK is installed: sudo apt install default-jdk"
    exit 1
fi

echo "[2/2] Compilation successful!"
echo ""
echo "Starting Racing Car..."
echo ""

# Run the game
java -cp out game.Main
