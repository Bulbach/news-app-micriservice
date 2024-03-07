package by.alex.newsappmicriservice.controller;

import by.alex.newsappmicriservice.dto.CommentDto;
import by.alex.newsappmicriservice.dto.RequestNewsDto;
import by.alex.newsappmicriservice.dto.ResponseNewsDto;
import by.alex.newsappmicriservice.dto.ResponseNewsDtoWithComments;
import by.alex.newsappmicriservice.service.NewsService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = NewsController.class)
public class NewsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NewsService<ResponseNewsDto, RequestNewsDto> newsService;

    @Test
    public void getNewsById_success() throws Exception {
        Long newsId = 1L;
        ResponseNewsDto news = new ResponseNewsDto(newsId, LocalDateTime.MAX, "Test News", "This is a test news");
        Mockito.when(newsService.findById(newsId)).thenReturn(news);

        mockMvc.perform(get("/news/{id}", newsId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(newsId))
                .andExpect(jsonPath("$.title").value(news.title()))
                .andExpect(jsonPath("$.text").value(news.text()));
    }

    @Test
    public void createNews_success() throws Exception {
        RequestNewsDto requestNews = new RequestNewsDto(null, LocalDateTime.MAX, "New News", "This is a new news");
        ResponseNewsDto createdNews = new ResponseNewsDto(2L, requestNews.time(), requestNews.title(), requestNews.text());
        Mockito.when(newsService.create(requestNews)).thenReturn(createdNews);

        mockMvc.perform(post("/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestNews)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(createdNews.id()))
                .andExpect(jsonPath("$.title").value(createdNews.title()))
                .andExpect(jsonPath("$.text").value(createdNews.text()));
    }

    @Test
    public void updateNews_success() throws Exception {
        Long newsId = 1L;
        RequestNewsDto requestNews = new RequestNewsDto(null, LocalDateTime.MAX, "Updated News", "This is an updated news");
        ResponseNewsDto updatedNews = new ResponseNewsDto(newsId, LocalDateTime.MAX, requestNews.title(), requestNews.text());
        Mockito.when(newsService.update(requestNews)).thenReturn(updatedNews);

        mockMvc.perform(put("/news/{id}", newsId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestNews)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedNews.id()))
                .andExpect(jsonPath("$.title").value(updatedNews.title()))
                .andExpect(jsonPath("$.text").value(updatedNews.text()));
    }

    @Test
    public void deleteNews_success() throws Exception {
        Long newsId = 1L;
        Mockito.doNothing().when(newsService).delete(newsId);

        mockMvc.perform(delete("/news/{id}", newsId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void getAllNews_success() throws Exception {
        List<ResponseNewsDto> newsList = List.of(
                new ResponseNewsDto(1L, LocalDateTime.MAX, "News 1", "This is news 1"),
                new ResponseNewsDto(2L, LocalDateTime.MAX, "News 2", "This is news 2")
        );
        Mockito.when(newsService.findAll(0, 10)).thenReturn(newsList);

        mockMvc.perform(get("/news")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void getNewsByIdWithAllComments_success() throws Exception {
        Long newsId = 1L;
        ResponseNewsDtoWithComments newsWithComments = ResponseNewsDtoWithComments.builder()
                .id(newsId)
                .time(LocalDateTime.MAX)
                .title("News with comments")
                .text("This is a news with comments")
                .commentDto(List.of(
                        CommentDto.builder().id(1L).time(LocalDateTime.MIN).text("Comment 1").username("Alex").newsId("2").build(),
                        CommentDto.builder().id(2L).time(LocalDateTime.MIN).text("Comment 2").username("Lelia").newsId("3").build()
                ))
                .build();


        Mockito.when(newsService.findNewsWithComments(newsId, 0, 100)).thenReturn(newsWithComments);

        mockMvc.perform(get("/news/{id}/comments", newsId)
                        .param("page", "0")
                        .param("size", "100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(newsId))
                .andExpect(jsonPath("$.commentDto", hasSize(2)));
    }
}

