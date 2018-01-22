plugins {
    java
}

repositories {
    mavenCentral()
}

val jooqVersion = "3.10.1"

dependencies {
    compile("org.jooq:jooq:$jooqVersion")
    compile("org.jooq:jooq-meta:$jooqVersion")
    compile("org.jooq:jooq-codegen:$jooqVersion")

    compile("org.xerial:sqlite-jdbc:3.21.0")
}

val java = the<JavaPluginConvention>()
java.sourceCompatibility = JavaVersion.VERSION_1_8

task("copyToLib", Copy::class) {
    into("$projectDir/lib")
    from(configurations.runtime)
}
