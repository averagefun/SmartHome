FROM eclipse-temurin:21
EXPOSE 8080:8080
RUN mkdir /app
COPY ./build/libs/MobileBackendFat.jar /app/MobileBackend.jar
ENTRYPOINT ["java", "-Dconfig.file=application-docker.conf", "-jar","/app/MobileBackend.jar"]
