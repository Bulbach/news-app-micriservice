package by.alex.newsappmicriservice.service;

import java.util.Collection;
import java.util.Optional;

public interface NewsService<T, K> {

    Collection<T> findAll(int page, int size);

    T findById(Long id);

    T create(K item);

    T update(K item);

    void delete(Long id);
}
