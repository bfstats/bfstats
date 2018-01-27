set projectName=dbfiller
set projectPath="subprojects/%projectName%"
set mainClass=io.github.bfstats.dbfiller.DbFiller

call gradlew.bat :%projectName%:build :%projectName%:copyToLib

echo Now starting to run %projectName%

java -classpath %projectPath%/lib/*;%projectPath%/build/classes/java/main;%projectPath%/build/resources/main %mainClass%
