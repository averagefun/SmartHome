FROM eclipse-temurin:21
EXPOSE 8090:8090
RUN mkdir /app
COPY ./build/libs/EmulationBackendFat.jar /app/EmulationBackend.jar
COPY ./src/main/resources/application-docker.conf /app/application-docker.conf
ENTRYPOINT ["java", "-Dconfig.file=/app/application-docker.conf", "-jar","/app/EmulationBackend.jar"]
