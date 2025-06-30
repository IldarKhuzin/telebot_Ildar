# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Папка для временных отчетов
RUN mkdir /app/reports

# Переменные окружения (могут быть переопределены в docker-compose)
ENV DB_URL=jdbc:postgresql://db:5432/surveydb
ENV DB_USER=surveyuser
ENV DB_PASSWORD=surveypass
ENV TELEGRAM_BOT_TOKEN=your_token_here

VOLUME ["/app/reports"]

ENTRYPOINT ["java", "-jar", "app.jar"]