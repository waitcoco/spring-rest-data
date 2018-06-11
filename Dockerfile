FROM openjdk:8-alpine
WORKDIR /app
RUN mkdir tdb_folder
COPY target/miami-publish-1.0.0.jar .
ENTRYPOINT ["java", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap", "-jar", "miami-publish-1.0.0.jar"]