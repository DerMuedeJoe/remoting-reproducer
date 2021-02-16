plugins {
    id("ear")
}

dependencies {
    earlib(project(":echo-ejb-api"))
    earlib("org.slf4j:slf4j-api:1.7.30")

    deploy(project( ":echo-ejb"))
}

ear {
    deploymentDescriptor {
        libDirName = "APP-INF/lib"
    }
}
