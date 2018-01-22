plugins {
    java
}

repositories {
    mavenCentral()
    gradleScriptKotlin()
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.16.18")

    compile(project(":subprojects/logparser"))
    compile(project(":subprojects/dbstats"))

    compile("org.slf4j:slf4j-api:1.7.22")
    compile("ch.qos.logback:logback-classic:1.0.13")
    compile("com.google.code.findbugs:jsr305:3.0.1")
    compile("commons-net:commons-net:3.6")
}




val java = the<JavaPluginConvention>()
java.sourceSets.getByName("main").resources.srcDir("../../config")
java.sourceCompatibility = JavaVersion.VERSION_1_8

task("copyToLib", Copy::class) {
    into("$projectDir/lib")
    from(configurations.runtime)
}
