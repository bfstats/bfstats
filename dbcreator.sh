#!/usr/bin/env sh

projectName="dbcreator"
projectPath="subprojects/$projectName"
mainClass="io.github.bfstats.dbcreator.DBCreator"

./gradlew :$projectName:build :$projectName:copyToLib

echo Now starting to run $projectPath

java -classpath $projectPath/lib/*:$projectPath/build/classes/java/main:$projectPath/build/resources/main $mainClass
