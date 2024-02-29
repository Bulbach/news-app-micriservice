package by.alex.newsappmicriservice.cache.impl;


import by.alex.newsappmicriservice.cache.AbstractCache;

import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;

/**
 * Класс, реализующий LRU (Least Recently Used) кэш.
 * LRU кэш используется для хранения данных, которые недавно запрашивались.
 * Если кэш заполнен, то элемент, который был запрошен последним, будет удален.
 *
 * @param <K> Тип ключей в кэше.
 * @param <V> Тип значений в кэше.
 */
public class LRUCache<K, V> implements AbstractCache<K, V> {

    /**
     * Переменная, которая определяет максимальный размер кэша.
     */
    private final int capacity;
    /**
     * Используется для хранения ключей и значений элементов кэша.
     */
    private final Map<K, V> cache;
    /**
     * Для сохранения порядка использования
     */
    private final Map<K, Integer> accessOrder;
    private final Deque<K> accessQueue;

    /**
     * Конструктор для создания нового экземпляра LRUCache с заданной емкостью.
     *
     * @param capacity Максимальный размер кэша.
     */
    public  LRUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>();
        this.accessOrder = new HashMap<>();
        this.accessQueue = new LinkedList<>();
    }

    /**
     * Получает значение из кэша по ключу.
     * Если элемент существует, его порядок использования обновляется.
     *
     * @param key Ключ элемента.
     * @return Значение элемента или null, если элемент не найден.
     */
    public V get(K key) {
        return Optional.ofNullable(cache.get(key))
                .map(value -> {
                updateAccessOrder(key);
                    return value;
                })
                .orElse(null);
    }

    /**
     * Добавляет новый элемент в кэш или обновляет существующий.
     * Если кэш заполнен, удаляет элемент, который был запрошен последним.
     *
     * @param key   Ключ элемента.
     * @param value Значение элемента.
     */
    public void put(K key, V value) {
        if (cache.containsKey(key)) {
            updateAccessOrder(key);
        } else {
            if (cache.size() >= capacity) {
                evict();
            }
            cache.put(key, value);
            updateAccessOrder(key);
        }
    }

    /**
     * Возвращает все значения из кэша.
     *
     * @return Коллекция значений из кэша.
     */
    public Collection<V> getAllValues() {
        return cache.values();
    }

    /**
     * Обновляет порядок использования элемента в кэше.
     *
     * @param key Ключ элемента.
     */
    private void updateAccessOrder(K key) {
        accessOrder.put(key, accessOrder.size() + 1);
        accessQueue.remove(key);
        accessQueue.addLast(key);
    }

    /**
     * Удаляет элемент из кэша по ключу.
     *
     * @param key Ключ элемента.
     */
    public void delete(K key) {
        cache.remove(key);
        accessOrder.remove(key);
        accessQueue.remove(key);
    }

    /**
     * Удаляет элемент, который был запрошен последним, из кэша.
     */
    public void evict() {
        K leastRecentlyUsed = accessQueue.pollFirst();
        if (leastRecentlyUsed != null) {
            cache.remove(leastRecentlyUsed);
            accessOrder.remove(leastRecentlyUsed);
        }
    }

    /**
     * Проверяет, существует ли элемент с заданным ключом в кэше.
     *
     * @param key Ключ элемента.
     * @return true, если элемент существует, иначе false.
     */
    @Override
    public boolean containsKey(K key) {
        return cache.containsKey(key);
    }
}