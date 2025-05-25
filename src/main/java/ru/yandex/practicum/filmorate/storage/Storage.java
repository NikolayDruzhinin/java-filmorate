package ru.yandex.practicum.filmorate.storage;

import java.util.Collection;
import java.util.Optional;

public interface Storage<T> {
    Collection<T> get();

    Optional<T> get(long id);

    T create(T t);

    void update(T t);

    boolean delete(T t);
}
