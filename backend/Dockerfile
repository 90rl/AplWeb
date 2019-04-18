FROM maven:latest AS build
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean package

FROM java:8
COPY --from=build /usr/src/app/target/backend-1.0-SNAPSHOT.jar backend.jar
EXPOSE 8082 5005
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,address=5005,suspend=n", "-jar","/backend.jar"]
