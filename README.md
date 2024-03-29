# Задание.

Система управления новостями

* Разработать RESTful web-service, реализующей функционал для работы с системой управления новостями.

* Основные сущности:
news (новость) содержит поля: id, time, title, text и comments (list).
comment содержит поля: id, time, text, username и news_id.


1. Использовать Spring Boot 3.x, Java 17, Gradle и PostgreSQL.
2. Разработать API согласно подходам REST (UI не надо):
   * CRUD для работы с новостью
   * CRUD для работы с комментарием
   * просмотр списка новостей (с пагинацией)
   * просмотр новости с комментариями относящимися к ней (с пагинацией)
   * /news
   * /news/{newsId}
   * /news/{newsId}/comments
   * /news/{newsId}/comments/{commentsId}
   * полнотекстовый поиск по различным параметрам (для новостей и комментариев)
   * Для потенциально объемных запросов реализовать постраничность

3. Разместить проект в любом из публичных git-репозиториев (Bitbucket, github, gitlab)    
4. Код должен быть легко читаемый и понятный, с использованием паттернов проектирования  
5. Реализовать на основе Spring @Profile (e.g. test & prod) подключение к базам данных. 
6. Подключить liquibase:
- при запуске сервиса накатываются скрипты на рабочую БД (генерируются необходимые таблицы из одного файла и наполняются 
- таблицы данными из другого файла, 20 новостей и 10 комментариев, связанных с каждой новостью
- при запуске тестов должен подхватываться скрипт по генерации необходимых таблиц + накатить данные по заполнению 
- таблиц (третий файл)  
7.Создать реализацию кэша, для хранения сущностей. Реализовать два алгоритма  LRU и LFU. Алгоритм и максимальный размер 
- коллекции должны читаться из файла application.yml. Алгоритм работы с кешем:
  GET - ищем в кеше и если там данных нет, то достаем объект из dao, сохраняем в кеш и возвращаем
  POST - сохраняем в dao и потом сохраняем в кеше
  DELETE - удаляем из dao и потом удаляем в кеша
  PUT - обновление/вставка в dao и потом обновление/вставка в кеше.  
8.Весь код должен быть покрыт юнит-тестами (80%) (сервисный слой – 100%)
9.Реализовать логирование запрос-ответ в аспектном стиле (для слоя Controlles), а также логирование по уровням в 
отдельных слоях приложения, используя logback  
10.Предусмотреть обработку исключений и интерпретацию их согласно REST  
11.Все настройки должны быть вынесены в .yml  
12.Код должен быть документирован @JavaDoc, а назначение приложения и его интерфейс и настройки должны быть описаны 
в README.md файле  
13.Использовать Spring REST Docs или другие средства автоматического документирования (например asciidoctor 
https://asciidoctor.org/docs/asciidoctor-gradle-plugin/ и т.д) и/или Swagger (OpenAPI 3.0)  
14.Использовать testcontainers в тестах на persistence layer (для БД)  
15.Написать интеграционные тесты  
16.Использовать WireMock в тестах для слоя clients (разбиение на микросервисы)  
17.Использовать Docker (написать Dockerfile – для spring boot приложения, docker-compose.yml для поднятия БД и 
приложения в контейнерах и настроить взаимодействие между ними)




# Дополнительно.
1. Подключить кэш провайдер Redis (в docker) (в случае реализации, использовать @Profile для переключения между LRU/LFU и Redis)  
2. Spring Security:
   API для регистрации пользователей с ролями admin/journalist/subscriber
   Администратор (role admin) может производить CRUD-операции со всеми сущностями
   Журналист (role journalist) может добавлять и изменять/удалять только свои новости
   Подписчик (role subscriber) может добавлять и изменять/удалять только свои комментарии
   Незарегистрированные пользователи могут только просматривать новости и комментарии
   Создать отдельный микросервис с реляционной базой (postgreSQL) хранящей
   информацию о пользователях/ролях. Из главного микросервиса (отвечающего за
   новости) запрашивать эту информацию по  REST с использованием spring-cloud-
   feign-client.

3. Настроить Spring Cloud Config (вынести в отдельный сервис и настроить разрабатываемый сервис на получение их 
4. в зависимости от профиля)  
5. Реализацию логирования п.9 и обработку исключений п.10 вынести в отдельные
   spring-boot-starter-ы.
6. ** Сущности веб интерфейса (DTO) должны генерироваться при сборке проекта из .proto файлов (см. https://github.com/google/protobuf-gradle-plugin)

##Инструкция по запуску. 
1. Запускаем exception-spring-boot-starter-0.0.1-SNAPSHOT.jar находится в корне сервиса News.
* после запуска выполнить task publishToMavenLocal(однократно), для сохранения его в локальный мавен репозиторий.
2. После требуется запустить конфиг сервер cloud-config-app-micriservice(для получения данных для конфигурации с удаленного репозитория)
3. После запускаются оба микросервиса news-app-micriservice и comment-app-micriservice (в любом порядке)
4. После этого система должна быть работоспособна.