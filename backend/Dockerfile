FROM java:8

EXPOSE 8082 5005

COPY /target/backend-1.0-SNAPSHOT.jar backend.jar

ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,address=5005,suspend=n", "-jar","/backend.jar"]
