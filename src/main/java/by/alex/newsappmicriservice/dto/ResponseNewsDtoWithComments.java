package by.alex.newsappmicriservice.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
@Data
public class ResponseNewsDtoWithComments {


    private Long id;

    private LocalDateTime time;

    private String title;

    private String text;

    private List<CommentDto> commentDto;
}
