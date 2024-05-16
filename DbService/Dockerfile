FROM eclipse-temurin:21
EXPOSE 8060:8060
RUN mkdir /app
COPY ./build/libs/DbServiceFat.jar /app/DbService.jar
COPY ./src/main/resources/application-docker.conf /app/application-docker.conf
ENTRYPOINT ["java", "-Dconfig.file=/app/application-docker.conf", "-jar","/app/DbService.jar"]
