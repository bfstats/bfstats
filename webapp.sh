./gradlew :subprojects/webapp:build
./gradlew :subprojects/webapp:copyToLib

java -classpath subprojects/webapp/lib/*:subprojects/webapp/build/classes/main:subprojects/webapp/build/resources/main io.github.bfvstats.Launcher