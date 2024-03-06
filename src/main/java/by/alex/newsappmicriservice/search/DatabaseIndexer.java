package by.alex.newsappmicriservice.search;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.massindexing.MassIndexer;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DatabaseIndexer {


    private final EntityManagerFactory entityManagerFactory;

    /**
     * Method annotated with {@code @PostConstruct} to perform data indexing upon bean initialization.
     *
     * @throws InterruptedException If the indexing process is interrupted.
     */
    @PostConstruct
    @Transactional
    void indexData() throws InterruptedException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        SearchSession searchSession = Search.session(entityManager);
        MassIndexer indexer = searchSession.massIndexer();
        indexer.startAndWait();
    }
}
