package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.Collection;
import java.util.Comparator;

@Service
@AllArgsConstructor
public class FilmService {
    private final Storage<Film> filmStorage;
    private final Storage<User> userStorage;

    public void makeLike(long filmId, long userId) {
        userStorage.checkIdExist(userId);
        filmStorage.checkIdExist(filmId);
        filmStorage.get(filmId).getUsersLikes().add(userId);
    }

    public void deleteLike(long filmId, long userId) {
        userStorage.checkIdExist(userId);
        filmStorage.checkIdExist(filmId);
        filmStorage.get(filmId).getUsersLikes().remove(userId);
    }

    public Collection<Film> getTopFilms(int count) {
        return filmStorage.get().stream()
                .sorted(Comparator.comparingInt(film -> -film.getUsersLikes().size()))
                .limit(count)
                .toList();
    }
}
