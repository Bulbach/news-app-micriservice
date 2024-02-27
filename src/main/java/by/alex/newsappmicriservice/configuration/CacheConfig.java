package by.alex.newsappmicriservice.configuration;

import by.alex.newsappmicriservice.cache.AbstractCache;
import by.alex.newsappmicriservice.cache.impl.LFUCache;
import by.alex.newsappmicriservice.cache.impl.LRUCache;
import by.alex.newsappmicriservice.dto.ResponseNewsDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ConfigurationProperties(prefix = "spring.cache")
public class CacheConfig {

    private String algorithm;

    private int max_size;


    @Bean
    public AbstractCache<Long, ResponseNewsDto> newsCache() {
        return "LFU".equals(algorithm)
                ? new LFUCache<>(max_size)
                : new LRUCache<>(max_size);
    }

 }
