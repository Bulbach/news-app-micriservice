package by.alex.newsappmicriservice.dto;

import java.time.LocalDateTime;

/**
 * Класс, представляющий данные для создания или обновления новости.
 *
 * @param id    Идентификатор новости.
 * @param time  Время создания новости.
 * @param title Заголовок новости.
 * @param text  Текст новости.
 */
public record RequestNewsDto(

        Long id,
        LocalDateTime time,
        String title,
        String text

) {
}
