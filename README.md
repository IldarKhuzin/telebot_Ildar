# telegram-survey-bot

Java Spring Boot Telegram-бот для проведения опросов и генерации отчетов.

## Структура проекта

```
telegram-survey-bot/
├── src/
│   ├── main/
│   │   ├── java/ru/ildar/surveybot/
│   │   │   ├── config/
│   │   │   │   ├── AsyncConfig.java
│   │   │   │   ├── BotConfig.java
│   │   │   │   ├── DatabaseConfig.java
│   │   │   │   └── ReportConfig.java
│   │   │   ├── controller/
│   │   │   │   ├── BotController.java
│   │   │   │   └── ReportController.java
│   │   │   ├── dto/
│   │   │   │   ├── SurveyDto.java
│   │   │   │   └── UserDto.java
│   │   │   ├── entity/
│   │   │   │   ├── SurveyEntity.java
│   │   │   │   └── UserEntity.java
│   │   │   ├── enums/
│   │   │   │   └── BotState.java
│   │   │   ├── exception/
│   │   │   │   ├── ReportGenerationException.java
│   │   │   │   └── SurveyValidationException.java
│   │   │   ├── repository/
│   │   │   │   ├── SurveyRepository.java
│   │   │   │   └── UserRepository.java
│   │   │   ├── service/
│   │   │   │   ├── ReportService.java
│   │   │   │   ├── SurveyService.java
│   │   │   │   ├── UserService.java
│   │   │   │   └── impl/
│   │   │   ├── util/
│   │   │   │   ├── EmailValidator.java
│   │   │   │   └── ReportGenerator.java
│   │   │   └── SurveyBotApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── messages.properties
│   └── test/
├── docker/
│   └── entrypoint.sh
├── Dockerfile
├── docker-compose.yml
└── README.md
```

## Описание
Spring Boot приложение для Telegram-бота:
- Опрос пользователей (/form: имя, email, оценка 1-10)
- Валидация email
- Генерация отчёта в Word (/report)
- Асинхронная обработка отчётов
- Сброс состояния при /start или /report
- Сохранение данных в PostgreSQL

## Доступные команды бота
- `/start` — приветственное сообщение
- `/form` — начать опрос (имя, email, оценка)
- `/report` — получить Word-отчёт с результатами

## Быстрый старт (Docker Compose)
1. Соберите и запустите проект:
   ```bash
   docker-compose up --build
   ```
2. Бот автоматически подключится к PostgreSQL (db: postgres, user: postgres, pass: postgres).
3. Для остановки:
   ```bash
   docker-compose down
   ```

## Переменные окружения
- `DB_URL` — строка подключения к БД (по умолчанию: jdbc:postgresql://db:5432/postgres)
- `DB_USER` — пользователь БД (по умолчанию: postgres)
- `DB_PASSWORD` — пароль БД (по умолчанию: postgres)

## Пример отчёта
| Имя | Email | Оценка |
|-----|-------|--------|
| ... | ...   | ...    |

---

**Для разработки:**
- Java 17+, Maven
- Spring Boot, Spring Data JPA, TelegramBots, Apache POI