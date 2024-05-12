FROM eclipse-temurin:21
EXPOSE 8060:8060
RUN mkdir /app
COPY ./build/libs/DbServiceFat.jar /app/DbService.jar
ENTRYPOINT ["java", "-Dconfig.file=application-docker.conf", "-jar","/app/DbService.jar"]
