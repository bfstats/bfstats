set projectName=jooq_gen
set projectPath="%projectName%"

cd ..
call gradlew.bat :%projectName%:build :%projectName%:copyToLib --info
cd jooq_gen
