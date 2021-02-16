plugins {
    id("java-library") // ugly hack for using jib...

    id ("com.google.cloud.tools.jib") version "2.7.1"
}

jib {
    from {
         image = "redhat.io/jboss-eap-7/eap72-openshift:1.2-23"
    }
    to {
        image = "test/jboss-server:1.0.0"
    }

    // the application as jar, not single class files
    containerizingMode = "packaged"

    container {
        user = "jboss"

        appRoot = "/opt/app"
        ports = listOf("8080", "9990", "8787")
        entrypoint = listOf("/docker-entrypoint.sh")
        args = listOf("start")
    }

    extraDirectories {
        paths {
            path {
                setFrom(rootProject.file("config/docker").toPath())
                into = "/"
            }
            path {
                setFrom(file("${project(":ear").buildDir}/libs").toPath())
                into = "/asd" // somewhere as jib will change folder rights to root and jboss cannot handle this
            }
        }
        permissions = mapOf(
            "/docker-entrypoint.sh" to "755"
        )
    }
}

tasks.jib {
    dependsOn(":ear:ear")
}
tasks.jibBuildTar {
    dependsOn(":ear:ear")
}
tasks.jibDockerBuild {
    dependsOn(":ear:ear")
}