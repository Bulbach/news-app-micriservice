package by.alex.newsappmicriservice.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
/**
 * Класс, представляющий DTO для новости с комментариями.
 * Используется для передачи данных о новости и ее комментариях между различными частями приложения.
 */
@Data
public class ResponseNewsDtoWithComments {

    /**
     * Идентификатор новости.
     */
    private Long id;
    /**
     * Время создания новости.
     */
    private LocalDateTime time;
    /**
     * Заголовок новости.
     */
    private String title;
    /**
     * Текст новости.
     */
    private String text;
    /**
     * Список комментариев к новости.
     */
    private List<CommentDto> commentDto;
}
