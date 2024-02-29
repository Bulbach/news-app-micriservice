package by.alex.newsappmicriservice.repository;

import by.alex.newsappmicriservice.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 * Интерфейс для работы с репозиторием новостей.
 * Расширяет {@link JpaRepository}, предоставляя методы для работы с новостями.
 */
@Repository
public interface NewsRepository extends JpaRepository<News,Long> {
}
