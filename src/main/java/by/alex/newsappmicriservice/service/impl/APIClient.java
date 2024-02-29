package by.alex.newsappmicriservice.service.impl;

import by.alex.newsappmicriservice.dto.CommentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
/**
 * Интерфейс для работы с Feign клиентом для взаимодействия с сервисом комментариев.
 * Аннотация {@link FeignClient} указывает, что этот интерфейс будет использоваться для создания Feign клиента.
 */
@FeignClient(name = "news", url = "http://localhost:8082/comments")
public interface APIClient {

    /**
     * Получает список комментариев к новости по ее идентификатору.
     *
     * @param newsId Идентификатор новости.
     * @param size   Размер страницы комментариев.
     * @param page   Номер страницы комментариев.
     * @return Список комментариев к новости.
     */
    @GetMapping("/news/{newsId}")
    List<CommentDto> getCommentsByNewsId(@PathVariable("newsId") Long newsId, @RequestParam("size") int size, @RequestParam("page") int page);
}
