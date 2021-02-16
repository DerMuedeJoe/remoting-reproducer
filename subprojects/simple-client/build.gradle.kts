plugins {
    id("java-library")
}

val slf4jApiVersion = "1.7.30"
val wildflyVersion = "7.2.9.GA"

dependencies {
    implementation (project(":echo-ejb-api"))
    implementation("org.slf4j:slf4j-api:${slf4jApiVersion}")

    implementation(enforcedPlatform("org.jboss.eap:wildfly-ejb-client-bom:${wildflyVersion}"))
    implementation("org.wildfly.wildfly-http-client:wildfly-http-ejb-client")
}
