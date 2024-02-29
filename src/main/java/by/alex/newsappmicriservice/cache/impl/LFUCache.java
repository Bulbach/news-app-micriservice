package by.alex.newsappmicriservice.cache.impl;


import by.alex.newsappmicriservice.cache.AbstractCache;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Класс, реализующий LFU (Least Frequently Used) кэш.
 * LFU кэш используется для хранения данных, которые чаще всего запрашиваются.
 * Если кэш заполнен, то элемент с наименьшей частотой использования будет удален.
 *
 * @param <K> Тип ключей в кэше.
 * @param <V> Тип значений в кэше.
 */
public class LFUCache<K, V> implements AbstractCache<K, V> {

    /**
     * Переменная, которая определяет максимальный размер кэша.
     */
    private final int capacity;
    /**
     * Используется для хранения ключей и значений элементов кэша.
     */
    private final Map<K, V> cache;
    /**
     * Используется для отслеживания частоты использования каждого элемента кэша.
     * Ключом является элемент кэша, а значением - его частота использования.
     */
    private final Map<K, Integer> frequency;
    /**
     * Используется для группировки элементов кэша
     * по их частоте использования. Ключом является частота использования,
     * а значением - LinkedHashSet элементов(для сохранения порядка элементов)
     * имеющих данную частоту использования.
     */
    private final Map<Integer, LinkedHashSet<K>> frequencyLists;

    /**
     * Конструктор для создания нового экземпляра LFUCache с заданной емкостью.
     *
     * @param capacity Максимальный размер кэша.
     */
    public LFUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>();
        this.frequency = new HashMap<>();
        this.frequencyLists = new HashMap<>();
        this.frequencyLists.put(1, new LinkedHashSet<>());
    }

    /**
     * Получает значение из кэша по ключу.
     * Если элемент существует, его частота использования увеличивается.
     *
     * @param key Ключ элемента.
     * @return Значение элемента или null, если элемент не найден.
     */
    public V get(K key) {
        return Optional.ofNullable(cache.get(key))
                .map(value -> {
                    updateFrequency(key);
                    return value;
                })
                .orElse(null);
    }

    /**
     * Добавляет новый элемент в кэш или обновляет существующий.
     * Если кэш заполнен, удаляет элемент с наименьшей частотой использования.
     *
     * @param key   Ключ элемента.
     * @param value Значение элемента.
     */
    public void put(K key, V value) {
        cache.computeIfPresent(key, (k, v) -> {
            updateFrequency(k);
            return value;
        });
        if (!cache.containsKey(key)) {
            if (cache.size() >= capacity) {
                evict();
            }
            frequency.put(key, 1);
            frequencyLists.computeIfAbsent(1, k2 -> new LinkedHashSet<>()).add(key);
            cache.put(key, value);
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
     * Обновляет частоту использования элемента в кэше.
     *
     * @param key Ключ элемента.
     */
    private void updateFrequency(K key) {
        frequency.computeIfPresent(key, (k, v) -> {
            int freq = v + 1;
            frequencyLists.computeIfAbsent(freq, k2 -> new LinkedHashSet<>()).add(key);
            frequencyLists.computeIfPresent(v, (k2, set) -> {
                set.remove(key);
                return set;
            });
            return freq;
        });
    }

    /**
     * Удаляет элемент из кэша по ключу.
     *
     * @param key Ключ элемента.
     */
    public void delete(K key) {
        cache.remove(key);
        frequency.remove(key);
        frequencyLists.values().forEach(set -> set.remove(key));
    }

    /**
     * Удаляет элемент с наименьшей частотой использования из кэша.
     */
    public void evict() {
        if (cache.size() >= capacity) {
            int minFreq = frequencyLists.keySet().stream().min(Integer::compareTo).orElse(0);
            Set<K> keysWithMinFreq = frequencyLists.get(minFreq);
            if (keysWithMinFreq != null && !keysWithMinFreq.isEmpty()) {
                K evictKey = keysWithMinFreq.iterator().next();
                keysWithMinFreq.remove(evictKey);
                cache.remove(evictKey);
                frequency.remove(evictKey);
            }
        }
    }

    /**
     * Проверяет, существует ли элемент с заданным ключом в кэше.
     *
     * @param id Ключ элемента.
     * @return true, если элемент существует, иначе false.
     */
    public boolean containsKey(K id) {
        return cache.containsKey(id);
    }

    /**
     * Возвращает значение по умолчанию, которое возвращается, если элемент не найден в кэше.
     *
     * @return Значение по умолчанию.
     */
    private String defaultValue() {
        return "Value not found";
    }
}

