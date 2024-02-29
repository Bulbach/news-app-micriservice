package by.alex.newsappmicriservice.cache.aspect;

import by.alex.newsappmicriservice.cache.AbstractCache;
import by.alex.newsappmicriservice.dto.ResponseNewsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Аспект для кеширования операций сервиса новостей.
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class NewsServiceCachingAspect {

    private final AbstractCache<Long, ResponseNewsDto> newsCache;

    /**
     * Pointcut, которая соответствует выполнению любого метода, аннотированного
     * с {@link by.alex.newsappmicriservice.cache.annotation.CustomCachableGet}.
     */
    @Pointcut("@annotation(by.alex.newsappmicriservice.cache.annotation.CustomCachableGet)")
    public void getId() {
    }

    /**
     * Совет, который кеширует результат выполнения метода, если он еще не кеширован.
     *
     * @param joinPoint точка соединения для advice метода
     * @return результат выполнения метода
     * @throws Throwable если возникает ошибка во время выполнения метода
     */
    @Around(value = "getId()")
    public Object cacheHouse(ProceedingJoinPoint joinPoint) throws Throwable {

        Long id = (Long) joinPoint.getArgs()[0];

        if (newsCache.containsKey(id)) {
            return newsCache.get(id);
        } else {
            ResponseNewsDto news = (ResponseNewsDto) joinPoint.proceed();
            log.info("Founded cache News in repository");
            newsCache.put(id, news);
            return news;
        }
    }

    /**
     * Pointcut, которая соответствует выполнению любого метода, аннотированного
     * с {@link by.alex.newsappmicriservice.cache.annotation.CustomCachebleCreate}.
     */
    @Pointcut("@annotation(by.alex.newsappmicriservice.cache.annotation.CustomCachebleCreate) ")
    public void create() {
    }

    /**
     * Совет, который кеширует созданную новость.
     *
     * @param joinPoint точка соединения для советного метода
     * @return результат выполнения метода
     * @throws Throwable если возникает ошибка во время выполнения метода
     */
    @Around(value = "create()")
    public Object cacheCreate(ProceedingJoinPoint joinPoint) throws Throwable {

        ResponseNewsDto createNews = (ResponseNewsDto) joinPoint.proceed();
        log.info("Created cache News " + createNews);
        newsCache.put(createNews.id(), createNews);

        return createNews;
    }

    /**
     * Pointcut, которая соответствует выполнению любого метода, аннотированного
     * с {@link by.alex.newsappmicriservice.cache.annotation.CustomCachebleUpdate}.
     */
    @Pointcut("@annotation(by.alex.newsappmicriservice.cache.annotation.CustomCachebleUpdate)")
    public void update() {
    }

    /**
     * Совет, который кеширует обновленную новость.
     *
     * @param joinPoint точка соединения для советного метода
     * @return результат выполнения метода
     * @throws Throwable если возникает ошибка во время выполнения метода
     */
    @Around(value = "update()")
    public Object cacheUpdate(ProceedingJoinPoint joinPoint) throws Throwable {

        ResponseNewsDto updateNews = (ResponseNewsDto) joinPoint.proceed();
        log.info("Updated cache News " + updateNews);
        newsCache.put(updateNews.id(), updateNews);
        return updateNews;
    }

    /**
     * Pointcut, которая соответствует выполнению любого метода, аннотированного
     * с {@link by.alex.newsappmicriservice.cache.annotation.CustomCachebleDelete}.
     */
    @Pointcut("@annotation(by.alex.newsappmicriservice.cache.annotation.CustomCachebleDelete)")
    public void delete() {
    }

    /**
     * Совет, который удаляет новость из кеша.
     *
     * @param joinPoint точка соединения для советного метода
     * @return результат выполнения метода
     * @throws Throwable если возникает ошибка во время выполнения метода
     */
    @Around(value = "delete()")
    public Object cacheDelete(ProceedingJoinPoint joinPoint) throws Throwable {

        Long id = (Long) joinPoint.getArgs()[0];
        log.info("Deleted cache News with id = " + id);
        newsCache.delete(id);

        return joinPoint.proceed();
    }
}
