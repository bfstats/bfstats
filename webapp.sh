#!/usr/bin/env sh

projectPath="subprojects/webapp"
mainClass="io.github.bfstats.Launcher"

./gradlew :$projectPath:build :$projectPath:copyToLib

echo Now starting to run $projectPath

java -classpath $projectPath/lib/*:$projectPath/build/classes/java/main:$projectPath/build/resources/main $mainClass
