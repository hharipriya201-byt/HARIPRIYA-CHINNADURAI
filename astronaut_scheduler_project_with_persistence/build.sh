#!/bin/bash
# Simple build script
mkdir -p out
javac -d out src/main/java/app/*.java
if [ $? -eq 0 ]; then
  echo "Build successful. Run with: java -cp out app.Main"
else
  echo "Build failed."
fi
