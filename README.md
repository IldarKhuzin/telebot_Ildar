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
│  
├── Dockerfile
├── docker-compose.yml
└── README.md
```

## Описание
Spring Boot приложение для Telegram-бота:
- Опрос пользователей (/form: имя, email, оценка 1-10)
- Валидация email
- Генерация отчёта в Word (/report)
- Асинхронная обработка отчётов (отчёт не блокирует работу бота)
- Сброс состояния при /start или /report
- Сохранение данных в PostgreSQL

## Доступные команды бота
- `/start` — приветственное сообщение и сброс состояния
- `/form` — начать опрос (имя, email, оценка)
- `/report` — получить Word-отчёт с результатами

## Быстрый старт через Docker Compose

1. **Создайте файл `.env` в корне проекта** (рядом с `docker-compose.yml`). Пример содержимого:

```
DB_URL=jdbc:postgresql://db:5432/postgres
DB_USER=postgres
DB_PASSWORD=postgres
POSTGRES_DB=postgres
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres
TELEGRAM_BOT_TOKEN=your_token_here
TELEGRAM_BOT_USERNAME=your_bot_username
SPRING_PROFILES_ACTIVE=dev
```

2. **Соберите и запустите проект:**
```bash
docker-compose up --build
```

3. **Для остановки контейнеров:**
```bash
docker-compose down
```

- Бот автоматически подключится к PostgreSQL (db: postgres, user: postgres, pass: postgres).
- Все параметры подключения и токены берутся из `.env`.
- Для хранения отчётов используется volume `./reports:/app/reports`.

**Важно:** Никогда не коммитьте свой `.env` в репозиторий!

## Переменные окружения

- `DB_URL` — строка подключения к БД (по умолчанию: `jdbc:postgresql://db:5432/postgres`)
- `DB_USER` — пользователь БД (по умолчанию: `postgres`)
- `DB_PASSWORD` — пароль пользователя БД (по умолчанию: `postgres`)
- `POSTGRES_DB` — имя БД для PostgreSQL (по умолчанию: `postgres`)
- `POSTGRES_USER` — пользователь PostgreSQL (по умолчанию: `postgres`)
- `POSTGRES_PASSWORD` — пароль PostgreSQL (по умолчанию: `postgres`)
- `TELEGRAM_BOT_TOKEN` — токен Telegram-бота
- `TELEGRAM_BOT_USERNAME` — username Telegram-бота
- `SPRING_PROFILES_ACTIVE` — профиль Spring Boot (`dev` или `prod`)

## Порт приложения

По умолчанию приложение запускается на порту 8081. Если вы запускаете через Docker Compose или локально, доступ будет по адресу:

    http://localhost:8081

## Пример отчёта
| Имя | Email | Оценка |
|-----|-------|--------|
| ... | ...   | ...    |

---

**Для разработки:**
- Java 17+, Maven
- Spring Boot, Spring Data JPA, TelegramBots, Apache POI

🧑‍💻 Автор
Ильдар, 2025
Проект реализован в рамках технического задания для прохождения собеседования.