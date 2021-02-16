# Remoting Example

The projects contains 2 server applications:

* JBoss EAP 7.2
* Spring Boot Application

![Architecture](docs/images/simple.png?raw=true "Generator")

## Project Structure

```shell
├── config
│    └── docker        # docker-entrypoint.sh
├── kubernetes         # deployment example in kubernetes
└── subprojects
    ├── ear            # Builds the ear to deply
    ├── echo-ejb       # Bean Impl
    ├── echo-ejb-api   # API
    ├── jboss-server   # JBoss Container builder
    └── spring-server  # Spring Server and Docker Container

```

### JBoss EAP

The JBoss application contains of one Service.

```java
public interface EchoServiceEjbApi {
   String ping();
}
```

The implementation will always simple return the String `pong`.

### Spring Boot Application

The Spring Boot Application exposes 2 Rest Endpoints simply doing remote calls to the JBoss Application server.

```shell
# Does a remote call to the JBoss Application server using http:
# http://jboss-service:8080/wildfly-services
/ping-http

# Does a remote call to the JBoss Application server using remote+http:
# remote+http://jboss-service:8080
/ping-remote-http
```

Allowing to be called by a simple curl:

```shell
curl http://<host>:<port>/ping-http
curl http://<host>:<port>/ping-remote-http
```



## Build and Run

### Build

```shell
./gradlew clean build jibDockerBuild
```

Two docker containers are build.
```shell
test/spring-server:1.0.0
test/jboss-server:1.0.0
```


### Run


#### Plain Docker

```shell
# Create some network
> docker network create service

# Run the JBoss Application
> docker container run -it --rm \
    --network service \
    --name jboss-service \
    test/jboss-server:1.0.0
  
# Run Spring boot application
> docker container run -it --rm \
    --network service \
    --name spring-service \
    -p 8080:8080 \
    test/spring-server:1.0.0
```

#### Kubernetes

See the `kubernetes` directory for a basic yaml

```shell
kubectl apply -f kubernetes/deployments.yaml
```