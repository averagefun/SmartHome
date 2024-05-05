FROM eclipse-temurin:21
EXPOSE 8090:8090
RUN mkdir /app
COPY ./build/libs/EmulationBackendFat.jar /app/EmulationBackend.jar
ENTRYPOINT ["java", "-Dconfig.file=application-docker.conf", "-jar","/app/EmulationBackend.jar"]