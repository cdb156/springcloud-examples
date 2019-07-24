FROM cjp/java:1.0
VOLUME /tmp
ADD springboot-docker-0.0.1.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]