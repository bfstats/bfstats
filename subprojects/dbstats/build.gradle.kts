plugins {
    java
}

repositories {
    mavenCentral()
}

val jooqVersion = "3.10.4"

dependencies {
    compile("org.jooq:jooq:$jooqVersion")
    compile("org.xerial:sqlite-jdbc:3.21.0")
}
