package by.alex.newsappmicriservice.repository;

import by.alex.newsappmicriservice.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<News,Long> {
}
