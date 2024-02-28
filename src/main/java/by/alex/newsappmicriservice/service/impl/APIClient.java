package by.alex.newsappmicriservice.service.impl;

import by.alex.newsappmicriservice.dto.CommentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "news", url = "http://localhost:8082/comments")
public interface APIClient {
    @GetMapping("/news/{newsId}")
    List<CommentDto> getCommentsByNewsId(@PathVariable("newsId") Long newsId, @RequestParam("size") int size, @RequestParam("page") int page);
}
