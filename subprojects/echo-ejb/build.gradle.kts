plugins {
    id("java-library")
}

val javaeeVersion = "7.0"
val slf4jApiVersion = "1.7.30"

dependencies {
    api(project(":echo-ejb-api"))

    implementation("org.slf4j:slf4j-api:${slf4jApiVersion}")

    compileOnly("javax:javaee-api:${javaeeVersion}")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}