package by.alex.newsappmicriservice.configuration;

import by.alex.newsappmicriservice.cache.AbstractCache;
import by.alex.newsappmicriservice.cache.impl.LFUCache;
import by.alex.newsappmicriservice.cache.impl.LRUCache;
import by.alex.newsappmicriservice.dto.ResponseNewsDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Класс конфигурации для настройки кэша новостей.
 * Используется для создания и настройки экземпляра кэша новостей.
 */
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "spring.cache")
public class CacheConfig {

    /**
     * Алгоритм кэширования, который будет использоваться для кэширования новостей.
     * Поддерживаемые алгоритмы: LFU (Least Frequently Used) и LRU (Least Recently Used).
     */
    private String algorithm;

    /**
     * Максимальный размер кэша новостей.
     */
    private int max_size;

    /**
     * Создает и настраивает экземпляр кэша новостей в зависимости от выбранного алгоритма.
     *
     * @return Экземпляр кэша новостей.
     */
    @Bean
    public AbstractCache<Long, ResponseNewsDto> newsCache() {
        return "LFU".equals(algorithm)
                ? new LFUCache<>(max_size)
                : new LRUCache<>(max_size);
    }

 }
