#!/bin/bash

echo "Compilation globale"

# -Xlint
# -Xdiags
javac -cp "../lib/*" -d ../build ../src/ldveh/**/*.java
