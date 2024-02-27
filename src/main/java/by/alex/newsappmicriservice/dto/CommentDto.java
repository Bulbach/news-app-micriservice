package by.alex.newsappmicriservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {
    private Long id;
    private LocalDateTime time;
    private String text;
    private String username;

}
