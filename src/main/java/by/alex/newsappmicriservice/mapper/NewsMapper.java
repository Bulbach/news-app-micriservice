package by.alex.newsappmicriservice.mapper;

import by.alex.newsappmicriservice.dto.RequestNewsDto;
import by.alex.newsappmicriservice.dto.ResponseNewsDto;
import by.alex.newsappmicriservice.entity.News;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface NewsMapper {

    News toModel(RequestNewsDto requestDto);

    ResponseNewsDto toDto(News news);

    void updateModel(RequestNewsDto requestDto, @MappingTarget News news);
}
