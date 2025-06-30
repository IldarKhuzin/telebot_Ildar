# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar /app/app.jar
ENV SPRING_PROFILES_ACTIVE=prod
ENTRYPOINT ["sh", "-c", "java -jar /app/app.jar --spring.profiles.active=${SPRING_PROFILES_ACTIVE}"]

# Папка для временных отчетов
RUN mkdir /app/reports

# Переменные окружения (могут быть переопределены в docker-compose)
ENV DB_URL=jdbc:postgresql://db:5432/surveydb
ENV DB_USER=postgres
ENV DB_PASSWORD=postgres
ENV TELEGRAM_BOT_TOKEN=7699108540:AAGK_HQugzmYct5JZua2gp_dXsJsMojLWKQ
ENV TELEGRAM_BOT_USERNAME:my_surveybot_bot

VOLUME ["/app/reports"]
