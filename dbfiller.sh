./gradlew :subprojects/dbfiller:build
./gradlew :subprojects/dbfiller:copyToLib

java -classpath subprojects/dbfiller/lib/*:subprojects/dbfiller/build/classes/main:subprojects/dbfiller/build/resources/main io.github.bfvstats.dbfiller.DbFiller