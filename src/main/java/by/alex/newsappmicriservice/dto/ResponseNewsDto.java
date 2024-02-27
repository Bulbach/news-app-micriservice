package by.alex.newsappmicriservice.dto;

import java.time.LocalDateTime;

public record ResponseNewsDto(
        Long id,
        LocalDateTime time,
        String title,
        String text
)
{}
