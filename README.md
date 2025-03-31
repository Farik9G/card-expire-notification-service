# Client Service

## Описание
Client Service — это микросервис, предназначенный для управления клиентами и их банковскими картами. Он позволяет создавать клиентов, управлять их картами и отправлять уведомления о сроке действия карт через Kafka.

## Технологии
- **Java 17**
- **Spring Boot**
- **Spring Data JPA**
- **Hibernate**
- **PostgreSQL**
- **Apache Kafka**
- **Lombok**
- **Docker**

## Переменные окружения
Для работы сервиса необходимо задать следующие переменные окружения:

```plaintext
POSTGRES_USER=your_username
POSTGRES_PASSWORD=your_password
POSTGRES_DB=client_db
DATASOURCE_URL=jdbc:postgresql://postgres:5432/client_db
DATASOURCE_USERNAME=your_username
DATASOURCE_PASSWORD=your_password
EMAIL_ADDRESS=your_email@example.com
EMAIL_PASSWORD=your_email_password
KAFKA_BOOTSTRAP_SERVERS=kafka:9092
```

## Установка и запуск

### Запуск через Docker
```sh
docker-compose up --build
```

### Локальный запуск
1. Убедитесь, что у вас установлен PostgreSQL и Kafka.
2. Запустите PostgreSQL и создайте базу данных `client_db`.
3. Запустите сервис с помощью Maven:
   ```sh
   mvn spring-boot:run
   ```

## API-эндпоинты

### Управление картами
- **PUT** `/cards/{cardId}/cancel` — отменяет активную карту пользователя.
- **POST** `/cards/{clientId}` — генерирует новую активную карту.
- **GET** `/cards/client/{clientId}` — получает все карты клиента.

### Управление клиентами
- **POST** `/clients` — создание нового клиента или получение существующего.
- **POST** `/clients/check-expiry` — запуск ручной проверки карт с истёкшим сроком.
- **GET** `/clients/{id}` — получить клиента по ID.

## 📚 Документация API (Swagger)

После запуска сервиса перейдите по адресу:

```
http://localhost:8081/swagger-ui.html
```

Там можно протестировать API и просмотреть подробности эндпоинтов.

## Тестирование
Для тестирования можно использовать Postman или выполнить запросы через `curl`:

```sh
curl -X POST http://localhost:8081/clients -H "Content-Type: application/json" -d '{"firstName": "Иван", "middleName": "Иванович", "lastName": "Иванов", "birthDate": "1999-12-26", "email": "ivanov@example.com}'
```
