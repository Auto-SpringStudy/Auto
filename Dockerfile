# Use an official JDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the application's JAR file to the container
COPY build/libs/demo-0.0.1-SNAPSHOT-plain.jar   demo-0.0.1-SNAPSHOT.jar   app.jar

# Expose the port your app runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

# Docker 환경에서 사용할 profile 설정
ENV SPRING_PROFILES_ACTIVE=docker

CMD ["java", "-Xmx512m", "-Xms256m", "-jar", "app.jar"]