#!/bin/bash

DIR=$(ls)
echo "${DIR}"

javac -d classes -cp '.:gson-2.8.6.jar' src/*.java
cd classes
java -cp '.:gson-2.8.6.jar' classes/src.Main