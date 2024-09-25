FROM openjdk:17-jdk

WORKDIR /app

COPY target/MyMallApplication-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
