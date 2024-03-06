package by.alex.newsappmicriservice.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Класс для получения результата от микросервиса Comment
 * при использовании feign-client
 */
@Data
@Builder
public class CommentDto {
    private Long id;
    private LocalDateTime time;
    private String text;
    private String username;
    private String newsId;
}
