FROM amazoncorretto:17-alpine-jdk
MAINTAINER github.com/wenqiglantz
EXPOSE 8100
COPY postgre.jks /tmp/postgre.jks
COPY target/customerservice-0.0.1-SNAPSHOT-exec.jar customerservice-0.0.1-SNAPSHOT-exec.jar
ENTRYPOINT ["java","-jar","/customerservice-0.0.1-SNAPSHOT-exec.jar"]