plugins {
    java
}

repositories {
    mavenCentral()
}

dependencies {
    compile("org.xerial:sqlite-jdbc:3.21.0")
}

val java = the<JavaPluginConvention>()
java.sourceSets.getByName("main").resources.srcDir("../../config")
java.sourceCompatibility = JavaVersion.VERSION_1_8

task("copyToLib", Copy::class) {
    into("$projectDir/lib")
    from(configurations.runtime)
}
