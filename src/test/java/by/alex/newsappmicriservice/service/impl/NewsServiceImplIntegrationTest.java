package by.alex.newsappmicriservice.service.impl;

import by.alex.newsappmicriservice.dto.ResponseNewsDto;
import by.alex.newsappmicriservice.entity.News;
import by.alex.newsappmicriservice.mapper.NewsMapper;
import by.alex.newsappmicriservice.repository.NewsRepository;
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.elasticsearch.action.admin.indices.refresh.RefreshResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestPropertySource(properties = {
        "spring.data.elasticsearch.cluster-nodes=http://${embedded.elasticsearch.host}:${embedded.elasticsearch.tcp.ports}",
        "search.field.title=title",
        "search.field.text=text",
        "search.boost.title=1.0f",
        "search.boost.text=1.0f"
})
class NewsServiceImplIntegrationTest {

    @Container
    private static final GenericContainer<?> elasticsearchContainer = new GenericContainer<>("docker.elastic.co/elasticsearch/elasticsearch:7.10.2")
            .withExposedPorts(9200, 9300)
            .waitingFor(Wait.forLogMessage(".*cluster has formed.*", 1));
    @Autowired
    private NewsServiceImpl newsService;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private NewsMapper newsMapper;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @LocalServerPort
    private int port;

    @Value("${embedded.elasticsearch.host}")
    private String elasticsearchHost;

    @BeforeEach
    void setUp() throws IOException {

        newsRepository.save(new News(1L, LocalDateTime.MAX, "News 1", "This is news 1"));
        newsRepository.save(new News(2L, LocalDateTime.MAX, "News 2", "This is news 2"));


        try {
            RefreshRequest refreshRequest = new RefreshRequest();
            RefreshResponse refreshResponse = restHighLevelClient.indices().refresh(refreshRequest, RequestOptions.DEFAULT);

        } catch (IOException e) {
            throw new RuntimeException("Crush to perform an Elasticsearch refresh ");
        }
    }

//    @Test
    void testSearch() {
        // Given
        String searchQuery = "Title";

        // When
        List<ResponseNewsDto> result = newsService.search(searchQuery, 0, 10);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result)
                .extracting(dto -> dto.title())
                .containsExactly("News 1", "News 2");
    }
}

