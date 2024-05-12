FROM eclipse-temurin:21
EXPOSE 8070:8070
RUN mkdir /app
COPY ./build/libs/LogServiceFat.jar /app/LogService.jar
ENTRYPOINT ["java", "-Dconfig.file=application-docker.conf", "-jar","/app/LogService.jar"]
