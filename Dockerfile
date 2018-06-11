FROM openjdk:8-alpine
WORKDIR /app
RUN mkdir tdb_folder
COPY target/miami-publish_1.0.0.jar .
ENTRYPOINT ["java", "-jar", "miami-publish_1.0.0.jar"]