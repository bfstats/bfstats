#!/usr/bin/env sh

projectName="webapp"
projectPath="subprojects/$projectName"
mainClass="io.github.bfstats.Launcher"

./gradlew :$projectName:build :$projectName:copyToLib

echo Now starting to run $projectName

java -classpath $projectPath/lib/*:$projectPath/build/classes/java/main:$projectPath/build/resources/main $mainClass
