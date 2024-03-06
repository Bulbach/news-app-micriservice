# Используем официальный образ Liquibase
FROM liquibase/liquibase:latest

# Копируем файлы из проекта
COPY . /liquibase

# Устанавливаем переменные окружения для Liquibase
ENV LIQUIBASE_URL=jdbc:postgresql://db:5432/myClevertecDB
ENV LIQUIBASE_USERNAME=alex
ENV LIQUIBASE_PASSWORD=alex
ENV LIQUIBASE_CHANGELOG_FILE=changelog.yaml

# Запускаем Liquibase
CMD ["--url=${LIQUIBASE_URL}", "--username=${LIQUIBASE_USERNAME}", "--password=${LIQUIBASE_PASSWORD}", "--changeLogFile=${LIQUIBASE_CHANGELOG_FILE}", "update"]