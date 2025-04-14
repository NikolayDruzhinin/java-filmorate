package ru.yandex.practicum.filmorate.storage;

import java.util.Collection;

public interface Storage<T> {
    Collection<T> get();

    T get(long id);

    T update(T film);

    T create(T film);

    void checkIdExist(long id);
}
