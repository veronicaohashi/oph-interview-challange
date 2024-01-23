FROM openjdk:17-jdk-slim-buster

WORKDIR /app

COPY build/libs/interview-challenge-0.0.1.jar /app/interview-challenge.jar

EXPOSE 8080

CMD ["java", "-jar", "interview-challenge.jar"]
