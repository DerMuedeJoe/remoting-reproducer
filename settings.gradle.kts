rootProject.name = "jboss-remoting"

include(":echo-ejb")
include(":echo-ejb-api")
include(":jboss-server")
include(":ear")
include(":simple-client")
include(":spring-server")

run {
    rootProject.children.forEach {
        it.projectDir = file("subprojects/${it.name}")
    }
}