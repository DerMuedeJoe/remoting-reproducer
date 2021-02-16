plugins {
    id("java-library")
    id ("com.google.cloud.tools.jib") version "2.7.1"
}

val wildflyVersion = "7.2.9.GA"
val springbootVersion = "2.4.2"

dependencies {
    implementation (project(":echo-ejb-api"))

    implementation (enforcedPlatform("org.springframework.boot:spring-boot-dependencies:${springbootVersion}"))
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation(enforcedPlatform("org.jboss.eap:wildfly-ejb-client-bom:${wildflyVersion}"))
    implementation("org.wildfly.wildfly-http-client:wildfly-http-ejb-client")

    implementation("org.springframework.boot:spring-boot-starter-log4j2")
    implementation("org.springframework.boot:spring-boot-starter-undertow")
}

configurations.all {
    exclude(module = "spring-boot-starter-logging")
    exclude(module = "spring-boot-starter-tomcat")
}


jib {
    from {
        image = "openjdk:11.0.10-jdk"
    }
    to {
        image = "test/spring-server:1.0.0"
    }

    container {
        ports = listOf("8080")
    }
}
