plugins {
    java
}

repositories {
    mavenCentral()
    gradleScriptKotlin()
}

dependencies {
    compile("org.xerial:sqlite-jdbc:3.21.0")
}

val java = the<JavaPluginConvention>()
java.sourceSets.getByName("main").resources.srcDir("../../config")
java.sourceCompatibility = JavaVersion.VERSION_1_8
