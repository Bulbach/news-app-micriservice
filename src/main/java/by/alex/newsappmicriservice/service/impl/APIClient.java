package by.alex.newsappmicriservice.service.impl;

import by.alex.newsappmicriservice.dto.CommentDto;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.List;

@FeignClient(name = "news")
public interface APIClient {

    List<CommentDto> getCommentsByNewsId(Long newsId);
}
