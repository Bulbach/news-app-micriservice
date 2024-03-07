package by.alex.newsappmicriservice.mapper;

import by.alex.newsappmicriservice.dto.RequestNewsDto;
import by.alex.newsappmicriservice.dto.ResponseNewsDto;
import by.alex.newsappmicriservice.entity.News;
import by.alex.newsappmicriservice.service.NewsService;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

/**
 * Интерфейс для маппинга между объектами DTO и моделями сущностей новостей.
 * Используется для преобразования данных между приходящих с UI, используемым в приложении,
 * используемым в базе данных.
 */
@Component
@Mapper(componentModel = "spring")
public interface NewsMapper {

    /**
     * Преобразует объект DTO запроса новости в модель сущности новости.
     *
     * @param requestDto Объект DTO запроса новости.
     * @return Модель сущности новости.
     */
    News toModel(RequestNewsDto requestDto);

    /**
     * Преобразует модель сущности новости в объект DTO ответа новости.
     *
     * @param news Модель сущности новости.
     * @return Объект DTO ответа новости.
     */
    ResponseNewsDto toDto(News news);

    /**
     * Обновляет модель сущности новости на основе данных из объекта DTO запроса новости.
     *
     * @param requestDto Объект DTO запроса новости.
     * @param news       Модель сущности новости, которую нужно обновить.
     */
    void updateModel(RequestNewsDto requestDto, @MappingTarget News news);
}
