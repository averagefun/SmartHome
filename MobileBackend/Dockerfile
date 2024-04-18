FROM eclipse-temurin:21
EXPOSE 8080:8080
RUN mkdir /app
COPY ./build/libs/MobileBackendFat.jar /app/MobileBackend.jar
ENTRYPOINT ["java","-jar","/app/MobileBackend.jar"]
