FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY /target/shop-service-1.0-SNAPSHOT-exec.jar .
ENTRYPOINT ["java","-jar","/shop-service-1.0-SNAPSHOT-exec.jar"]