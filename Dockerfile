FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/UserService.jar /app/UserService.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "UserService.jar"]
