FROM eclipse-temurin:21
EXPOSE 8091:8091
RUN mkdir /app
COPY ./build/libs/RequestMakerFat.jar /app/RequestMaker.jar
COPY ./src/main/resources/application-docker.conf /app/application-docker.conf
ENTRYPOINT ["java", "-Dconfig.file=/app/application-docker.conf", "-jar","/app/RequestMaker.jar"]
