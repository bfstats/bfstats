plugins {
    java
}

repositories {
    mavenCentral()
}

val jooqVersion = "3.10.4"

dependencies {
    compileOnly("org.projectlombok:lombok:1.16.18")
    testCompile("org.jooq:jooq-codegen:$jooqVersion")

    compile("org.jooq:jooq:$jooqVersion")
    compile("org.jooq:jooq-meta:$jooqVersion")

    compile("org.xerial:sqlite-jdbc:3.21.0")
}

