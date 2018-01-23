import org.gradle.api.tasks.Copy


plugins {
    java
}

val pippoVersion = "1.7.0"
val jooqVersion = "3.10.4"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.16.18")

    testCompile("junit:junit:4.12")
    testCompile("ro.pippo:pippo-test:$pippoVersion")

    compile("com.google.code.findbugs:jsr305:3.0.1")

    compile("org.slf4j:slf4j-api:1.7.22")
    compile("ch.qos.logback:logback-classic:1.0.13")

    compile("ro.pippo:pippo-core:$pippoVersion")
    compile("ro.pippo:pippo-controller:$pippoVersion")

    compile("com.google.inject:guice:4.1.0")
    compile("ro.pippo:pippo-guice:$pippoVersion")

    compile("ro.pippo:pippo-jackson:$pippoVersion")
    compile("ro.pippo:pippo-jaxb:$pippoVersion")
    compile("ro.pippo:pippo-pebble:$pippoVersion")
    compile("ro.pippo:pippo-undertow:$pippoVersion")

    compile("org.jooq:jooq:$jooqVersion")
    compile("org.jooq:jooq-meta:$jooqVersion")
    compile("org.jooq:jooq-codegen:$jooqVersion")

    compile("org.xerial:sqlite-jdbc:3.21.0")

    compile(project(":dbstats"))
}


val java = the<JavaPluginConvention>()
java.sourceSets.getByName("main").resources.srcDir("../../config")
java.sourceCompatibility = JavaVersion.VERSION_1_8

task("copyToLib", Copy::class) {
    into("$projectDir/lib")
    from(configurations.runtime)
}
