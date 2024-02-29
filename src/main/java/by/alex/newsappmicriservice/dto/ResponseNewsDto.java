package by.alex.newsappmicriservice.dto;

import java.time.LocalDateTime;
/**
 * Класс, представляющий ответное DTO для новости.
 *
 * @param id    Идентификатор новости.
 * @param time  Время создания новости.
 * @param title Заголовок новости.
 * @param text  Текст новости.
 */
public record ResponseNewsDto(
        Long id,
        LocalDateTime time,
        String title,
        String text
)
{}
