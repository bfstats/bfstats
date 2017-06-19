set projectPath=subprojects/webapp
set mainClass=io.github.bfvstats.Launcher

call gradlew.bat :%projectPath%:build :%projectPath%:copyToLib

echo Now starting to run %projectPath%

java -classpath %projectPath%/lib/*;%projectPath%/build/classes/java/main;%projectPath%/build/resources/main %mainClass%