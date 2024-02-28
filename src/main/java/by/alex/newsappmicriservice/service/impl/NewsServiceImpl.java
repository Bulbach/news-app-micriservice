package by.alex.newsappmicriservice.service.impl;

import by.alex.newsappmicriservice.cache.annotation.CustomCachableGet;
import by.alex.newsappmicriservice.cache.annotation.CustomCachebleCreate;
import by.alex.newsappmicriservice.cache.annotation.CustomCachebleDelete;
import by.alex.newsappmicriservice.cache.annotation.CustomCachebleUpdate;
import by.alex.newsappmicriservice.dto.CommentDto;
import by.alex.newsappmicriservice.dto.RequestNewsDto;
import by.alex.newsappmicriservice.dto.ResponseNewsDto;
import by.alex.newsappmicriservice.dto.ResponseNewsDtoWithComments;
import by.alex.newsappmicriservice.entity.News;
import by.alex.newsappmicriservice.mapper.NewsMapper;
import by.alex.newsappmicriservice.repository.NewsRepository;
import by.alex.newsappmicriservice.service.NewsService;
import by.bulbach.exceptionspringbootstarter.exception.InvalidRequestException;
import by.bulbach.exceptionspringbootstarter.exception.NewsNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService<ResponseNewsDto, RequestNewsDto> {

    private final NewsRepository repository;
    private final APIClient commentClient;
    @Qualifier("newsMapperImpl")
    private final NewsMapper mapper;

    @CustomCachableGet
    public List<ResponseNewsDto> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

    }

    @Override
    public ResponseNewsDto findById(Long id) {
        News news = repository.findById(id)
                .orElseThrow(() -> new NewsNotFoundException("News with id= " + id + "not found"));
        return mapper.toDto(news);
    }

    @Override
    @CustomCachebleCreate
    public ResponseNewsDto create(RequestNewsDto news) {
        if (news == null) {
            throw new InvalidRequestException("If you want to create News can`t be null");
        }

        News createdNews = mapper.toModel(news);
        News save = repository.save(createdNews);

        return mapper.toDto(save);
    }

    @Override
    @CustomCachebleUpdate
    public ResponseNewsDto update(RequestNewsDto news) {
        if (news == null) {
            throw new InvalidRequestException("If you want to create News can`t be null");
        }
        News newsById = repository.findById(news.id())
                .orElseThrow(() -> new NewsNotFoundException("news with id= " + news.id() + " not found"));
        mapper.updateModel(news, newsById);

        return mapper.toDto(repository.save(newsById));
    }

    @Override
    @CustomCachebleDelete
    public void delete(Long id) {
        if (id == 0) {
            throw new RuntimeException("Id can`t be null ");
        }
        News newsById = repository.findById(id)
                .orElseThrow(() -> new NewsNotFoundException("news with id= " + id + " not found"));

        repository.delete(newsById);
    }

    public ResponseNewsDtoWithComments findNewsWithComments(Long id, int page, int size) {
        ResponseNewsDtoWithComments withComments = new ResponseNewsDtoWithComments();
        News newsById = repository.findById(id)
                .orElseThrow(() -> new NewsNotFoundException("news with id= " + id + " not found"));
        withComments.setId(newsById.getId());
        withComments.setTime(newsById.getTime());
        withComments.setText(newsById.getText());
        withComments.setTitle(newsById.getTitle());
        List<CommentDto> commentsByNewsId = commentClient.getCommentsByNewsId(id,size,page);
        withComments.setCommentDto(commentsByNewsId);

        return withComments;
    }
}
