projectPath="subprojects/dbfiller"
mainClass="io.github.bfvstats.dbfiller.DbFiller"

./gradlew :$projectPath:build :$projectPath:copyToLib

echo Now starting to run %projectPath%

java -classpath $projectPath/lib/*:$projectPath/build/classes/main:$projectPath/build/resources/main $mainClass