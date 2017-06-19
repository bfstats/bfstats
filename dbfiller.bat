set projectPath=subprojects/dbfiller
set mainClass=io.github.bfvstats.dbfiller.DbFiller

call gradlew.bat :%projectPath%:build :%projectPath%:copyToLib

echo Now starting to run %projectPath%

java -classpath %projectPath%/lib/*;%projectPath%/build/classes/java/main;%projectPath%/build/resources/main %mainClass%