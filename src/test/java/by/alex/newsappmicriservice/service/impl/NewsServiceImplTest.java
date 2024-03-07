package by.alex.newsappmicriservice.service.impl;

import by.alex.newsappmicriservice.dto.CommentDto;
import by.alex.newsappmicriservice.dto.RequestNewsDto;
import by.alex.newsappmicriservice.dto.ResponseNewsDto;
import by.alex.newsappmicriservice.dto.ResponseNewsDtoWithComments;
import by.alex.newsappmicriservice.entity.News;
import by.alex.newsappmicriservice.mapper.NewsMapper;
import by.alex.newsappmicriservice.repository.NewsRepository;
import by.bulbach.exceptionspringbootstarter.exception.NewsNotFoundException;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NewsServiceImplTest {
    @Mock
    NewsRepository repository;
    @Mock
    EntityManagerFactory entityManagerFactory;
    @Mock
    APIClient commentClient;
    @Mock
    NewsMapper mapper;
    @Mock
    Logger log;
    @InjectMocks
    NewsServiceImpl newsService;


    @Test
    void findByIdShouldReturnExpectedNews() {

        // given
        News news = new News(1L, LocalDateTime.now(), "New News", "This is a new news");
        ResponseNewsDto responseDto = new ResponseNewsDto(news.getId(), news.getTime(), news.getTitle(), news.getText());
        ResponseNewsDto expected = new ResponseNewsDto(news.getId(), news.getTime(), news.getTitle(), news.getText());

        when(repository.findById(any(Long.class))).thenReturn(Optional.of(news));
        when(mapper.toDto(news)).thenReturn(responseDto);

        // when
        ResponseNewsDto actual = newsService.findById(news.getId());

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void testFindReallyAll() {
        // given
        int page = 0;
        int size = 2;
        Pageable pageable = PageRequest.of(page, size);
        List<News> newsList = List.of(
                new News(1L, LocalDateTime.MAX, "News 1", "This is news 1"),
                new News(2L, LocalDateTime.MAX, "News 2", "This is news 2")
        );
        Page<News> newsPage = new PageImpl<>(newsList, pageable, newsList.size());

        ResponseNewsDto responseDto = new ResponseNewsDto(1L, LocalDateTime.MAX, "News 1", "This is news 1");

        // when
        when(repository.findAll(any(Pageable.class))).thenReturn(newsPage);
        when(mapper.toDto(any(News.class))).thenReturn(responseDto);

        // when
        List<ResponseNewsDto> result = newsService.findAll(page, size);

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);

    }

    @Test
    void testCreate() {
        // given
        RequestNewsDto requestNews = new RequestNewsDto(null, LocalDateTime.MAX, "New News", "This is a new news");
        News createdNews = new News(1L, LocalDateTime.now(), "New News", "This is a new news");
        ResponseNewsDto responseDto = new ResponseNewsDto(createdNews.getId(), createdNews.getTime(), createdNews.getTitle(), createdNews.getText());

        when(mapper.toModel(requestNews)).thenReturn(createdNews);
        when(repository.save(createdNews)).thenReturn(createdNews);
        when(mapper.toDto(createdNews)).thenReturn(responseDto);

        // when
        ResponseNewsDto result = newsService.create(requestNews);

        // Then
        assertNotNull(result);
        assertEquals(responseDto, result);
    }


    @Test
    void testUpdate() {
        // Given
        RequestNewsDto requestNewsDto = new RequestNewsDto(1L, LocalDateTime.MAX, "Updated News", "This news has been updated");
        News existingNews = new News(1L, LocalDateTime.now(), "Old News", "This is the old news");
        News updatedNews = new News(1L, LocalDateTime.now(), "Updated News", "This news has been updated");
        ResponseNewsDto responseDto = new ResponseNewsDto(updatedNews.getId(), updatedNews.getTime(), updatedNews.getTitle(), updatedNews.getText());

        when(repository.findById(requestNewsDto.id())).thenReturn(Optional.of(existingNews));
        doNothing().when(mapper).updateModel(any(RequestNewsDto.class), any(News.class));
        when(repository.save(any(News.class))).thenReturn(updatedNews);
        when(mapper.toDto(updatedNews)).thenReturn(responseDto);

        // When
        ResponseNewsDto result = newsService.update(requestNewsDto);

        // Then
        assertNotNull(result);
        assertEquals(responseDto, result);
    }

    @Test
    void testFindById_NotFound() {
        // Given
        Long newsId = 1L;

        // When, Then
        assertThrows(NewsNotFoundException.class, () -> newsService.findById(newsId));
    }

    @Test
    void testDelete() {
        // Given
        Long newsId = 1L;
        News newsToDelete = new News(newsId, LocalDateTime.now(), "News to delete", "This news will be deleted");

        when(repository.findById(newsId)).thenReturn(Optional.of(newsToDelete));

        // When
        assertDoesNotThrow(() -> newsService.delete(newsId));

        // Then
        verify(repository, times(1)).delete(newsToDelete);
    }

    @Test
    void testFindNewsWithComments() {
        // given
        Long newsId = 1L;
        int page = 0;
        int size = 10;

        News news = new News(newsId, LocalDateTime.MAX, "News 1", "This is news 1");

        ResponseNewsDtoWithComments expectedResponse = ResponseNewsDtoWithComments.builder()
                .id(news.getId())
                .time(news.getTime())
                .title(news.getTitle())
                .text(news.getText())
                .commentDto(
                        List.of(CommentDto.builder()
                                .id(1L)
                                .time(LocalDateTime.MIN)
                                .text("This is comment 1")
                                .username("Alex Pomidorov")
                                .newsId("1").build())
                ).build();

        when(repository.findById(newsId)).thenReturn(Optional.of(news));

        when(commentClient.getCommentsByNewsId(newsId, size, page))
                .thenReturn(List.of(CommentDto.builder()
                        .id(1L)
                        .time(LocalDateTime.MIN)
                        .text("This is comment 1")
                        .username("Alex Pomidorov")
                        .newsId("1")
                        .build()));

        // when
        ResponseNewsDtoWithComments result = newsService.findNewsWithComments(newsId, page, size);

        // then
        assertNotNull(result);
        assertEquals(expectedResponse, result);
    }

    @Test
    void testFindNewsWithCommentById() {
        // given
        Long newsId = 1L;
        Long commentId = 2L;
        CommentDto expectedComment = CommentDto.builder()
                .id(1L)
                .time(LocalDateTime.MIN)
                .text("This is comment 1")
                .username("Alex Pomidorov")
                .newsId("1").build();

        when(commentClient.getCommentByNewsIdAndCommentId(newsId, commentId)).thenReturn(expectedComment);

        // when
        CommentDto result = newsService.findNewsWithCommentById(newsId, commentId);

        // then
        assertNotNull(result);
        assertEquals(expectedComment, result);
    }
}

