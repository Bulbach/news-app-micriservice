#FROM openjdk:17-alpine
#
#COPY . /app
#WORKDIR /app
#
##RUN ./gradlew build
#
#EXPOSE 8081
#WORKDIR /app
#
#CMD java -jar ./build/libs/news-app-micriservice-0.0.1-SNAPSHOT.jar

# Используем базовый образ с JDK
#FROM openjdk:17-alpine
#WORKDIR /app
#COPY . /app
#
# Экспорт порта
#EXPOSE 8081
#
# Команда для запуска приложения
#CMD ["java", "-jar", "news-app-micriservice-0.0.1-SNAPSHOT.jar"]

# Используем базовый образ с JRE
FROM openjdk:17-alpine

# Копируем JAR-файл в рабочую директорию
COPY build/libs/news-app-micriservice-0.0.1-SNAPSHOT.jar /app.jar

# Экспорт порта, если приложение должно быть доступно через сеть
EXPOSE 8081

# Команда для запуска приложения
ENTRYPOINT ["java","-jar","/app.jar"]


# Используем базовый образ с JDK
#FROM openjdk:17-alpine as builder

# Установка необходимых пакетов
#RUN apk add --no-cache bash curl

# Установка Gradle Wrapper
#WORKDIR /app
#COPY . /app

# Запуск сборки с помощью Gradle Wrapper
# Убедитесь, что локальный репозиторий Maven доступен внутри контейнера
#RUN ./gradlew build --no-daemon -Dmaven.repo.local=C:\Users\zvero\.m2\repository\by\bulbach\exception-spring-boot-starter\0.0.1-SNAPSHOT

# Используем базовый образ с JRE для запуска приложения
#FROM openjdk:17-alpine

# Копируем собранное приложение
#COPY --from=builder /app/build/libs/news-app-micriservice-0.0.1-SNAPSHOT.jar /app/

# Рабочая директория
#WORKDIR /app

# Экспорт порта
#EXPOSE 8081

# Команда для запуска приложения
#CMD ["java", "-jar", "news-app-micriservice-0.0.1-SNAPSHOT.jar"]
# Используем базовый образ с JDK
#FROM gradle:8.5-jdk17-alpine as builder
#
## Установка необходимых пакетов
#RUN apk add --no-cache bash curl
#
## Установка Gradle Wrapper
#WORKDIR /app
#COPY . /app
#
## Запуск сборки с помощью Gradle Wrapper
#RUN gradle build --no-daemon
#
## Используем базовый образ с JRE для запуска приложения
#FROM openjdk:17-alpine
#
## Копируем собранное приложение
#COPY --from=builder /app/build/libs/news-app-micriservice-0.0.1-SNAPSHOT.jar /app/
#
## Рабочая директория
#WORKDIR /app
#
## Экспорт порта
#EXPOSE 8081
#
## Команда для запуска приложения
#CMD ["java", "-jar", "news-app-micriservice-0.0.1-SNAPSHOT.jar"]
