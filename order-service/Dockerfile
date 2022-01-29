FROM amazoncorretto:17-alpine-jdk
MAINTAINER github.com/wenqiglantz
EXPOSE 8080
COPY postgre.jks /tmp/postgre.jks
COPY target/orderservice-0.0.1-SNAPSHOT-exec.jar orderservice-0.0.1-SNAPSHOT-exec.jar
ENTRYPOINT ["java","-jar","/orderservice-0.0.1-SNAPSHOT-exec.jar"]