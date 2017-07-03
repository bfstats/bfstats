plugins {
    java
}

repositories {
    mavenCentral()
    gradleScriptKotlin()
}

val jooqVersion = "3.9.0"

dependencies {
    compileOnly("org.projectlombok:lombok:1.16.12")
    testCompile("org.jooq:jooq-codegen:$jooqVersion")

    compile("org.jooq:jooq:$jooqVersion")
    compile("org.jooq:jooq-meta:$jooqVersion")

    compile("org.xerial:sqlite-jdbc:3.15.1")
}

