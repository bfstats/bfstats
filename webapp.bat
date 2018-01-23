set projectName=webapp
set projectPath="subprojects/%projectName%"

set mainClass=io.github.bfstats.Launcher

call gradlew.bat :%projectName%:build :%projectName%:copyToLib

echo Now starting to run %projectPath%

java -classpath %projectPath%/lib/*;%projectPath%/build/classes/java/main;%projectPath%/build/resources/main %mainClass%
