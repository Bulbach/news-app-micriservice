package by.alex.newsappmicriservice.service;

import by.alex.newsappmicriservice.dto.ResponseNewsDtoWithComments;
import by.bulbach.exceptionspringbootstarter.exception.InvalidRequestException;
import by.bulbach.exceptionspringbootstarter.exception.NewsNotFoundException;

import java.util.Collection;
import java.util.List;

/**
 * Интерфейс для работы с новостями.
 *
 * @param <T> тип ответа
 * @param <K> тип запроса на создание/обновление новости
 */
public interface NewsService<T, K> {

    /**
     * Возвращает коллекцию новостей на заданной странице с заданным размером.
     *
     * @param page номер страницы
     * @param size размер страницы
     * @return коллекция новостей
     */
    Collection<T> findAll(int page, int size);

    /**
     * Возвращает новость по идентификатору.
     *
     * @param id идентификатор новости
     * @return новость
     * @throws NewsNotFoundException если новость не найдена
     */
    T findById(Long id);

    /**
     * Создает новую новость.
     *
     * @param item запрос на создание новости
     * @return созданная новость
     * @throws InvalidRequestException если запрос некорректен
     */
    T create(K item);

    /**
     * Обновляет существующую новость.
     *
     * @param item запрос на обновление новости
     * @return обновленная новость
     * @throws NewsNotFoundException если новость не найдена
     */
    T update(K item);

    /**
     * Удаляет новость по идентификатору.
     *
     * @param id идентификатор новости
     * @throws NewsNotFoundException если новость не найдена
     */
    void delete(Long id);

    /**
     * Возвращает новость с комментариями на заданной странице с заданным размером.
     *
     * @param id   идентификатор новости
     * @param page номер страницы
     * @param size размер страницы
     * @return новость с комментариями
     * @throws NewsNotFoundException если новость не найдена
     */
    ResponseNewsDtoWithComments findNewsWithComments(Long id, int page, int size);

    Collection<T> findReallyAll();

    /**
     * Получает список всех новостей используя расширенный поиск с пагинацией.
     *
     * @param search Фрагмент строки для поиска.
     * @param page   Номер страницы.
     * @param size   Размер страницы.
     * @return Список DTO новостей.
     */
    List<T> search(String search, int page, int size);
}
