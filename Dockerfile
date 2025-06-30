# Stage 1: Build
FROM maven:3.8.7-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

# Папка для временных отчетов
RUN mkdir /app/reports

# Переменные окружения (могут быть переопределены в docker-compose)
ENV DB_URL=jdbc:postgresql://db:5432/surveydb
ENV DB_USER=surveyuser
ENV DB_PASSWORD=surveypass
ENV TELEGRAM_BOT_TOKEN=your_token_here

VOLUME ["/app/reports"]