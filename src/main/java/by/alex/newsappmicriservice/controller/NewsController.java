package by.alex.newsappmicriservice.controller;

import by.alex.newsappmicriservice.dto.RequestNewsDto;
import by.alex.newsappmicriservice.dto.ResponseNewsDto;
import by.alex.newsappmicriservice.dto.ResponseNewsDtoWithComments;
import by.alex.newsappmicriservice.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService<ResponseNewsDto, RequestNewsDto> newsService;

    @GetMapping("/{id}")
    public ResponseEntity<ResponseNewsDto> getNewsById(@PathVariable Long id) {
        ResponseNewsDto news = newsService.findById(id);
        return ResponseEntity.ok(news);
    }

    @PostMapping
    public ResponseEntity<ResponseNewsDto> createNews(@RequestBody RequestNewsDto news) {
        ResponseNewsDto createdNews = newsService.create(news);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdNews);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseNewsDto> updateNews(@PathVariable Long id, @RequestBody RequestNewsDto news) {
        ResponseNewsDto updatedNews = newsService.update(news);
        return ResponseEntity.ok(updatedNews);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNews(@PathVariable Long id) {
        newsService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ResponseNewsDto>> getAllNews(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        List<ResponseNewsDto> newsList = (List<ResponseNewsDto>) newsService.findAll(page, size);
        return ResponseEntity.ok(newsList);

    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<ResponseNewsDtoWithComments> getNewsByIdWithAllComments(@PathVariable Long id,
                                                                                  @RequestParam(defaultValue = "0") int page,
                                                                                  @RequestParam(defaultValue = "100") int size) {
        ResponseNewsDtoWithComments newsWithComments = newsService.findNewsWithComments(id,page,size);
        return ResponseEntity.ok(newsWithComments);
    }
}
