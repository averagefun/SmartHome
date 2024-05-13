FROM eclipse-temurin:21
EXPOSE 8080:8080
RUN mkdir /app
COPY ./build/libs/MobileBackendFat.jar /app/MobileBackend.jar
COPY ./src/main/resources/application-docker.conf /app/application-docker.conf
ENTRYPOINT ["java", "-Dconfig.file=/app/application-docker.conf", "-jar","/app/MobileBackend.jar"]
