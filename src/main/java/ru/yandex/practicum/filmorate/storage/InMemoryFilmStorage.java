package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Component
@Slf4j
public class InMemoryFilmStorage implements Storage<Film> {
    private final Map<Long, Film> films = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong(0);
    private final int maxDescriptionLength = 200;

    @Override
    public Collection<Film> get() {
        return films.values();
    }

    @Override
    public Film get(long id) {
        return films.get(id);
    }

    @Override
    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            log.error("Film with id {} not found", film.getId());
            throw new NotFoundException("Film with id " + film.getId() + " not found");
        }
        validate(film);

        Film oldFilm = films.get(film.getId());
        oldFilm.setDescription(film.getDescription());
        oldFilm.setName(film.getName());
        oldFilm.setDuration(film.getDuration());
        oldFilm.setReleaseDate(film.getReleaseDate());
        if (film.getUsersLikes() != null) {
            oldFilm.setUsersLikes(film.getUsersLikes());
        }
        log.debug("{} was updated", film);
        return film;
    }

    private void validate(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            InMemoryFilmStorage.log.error("Film name is empty");
            throw new ConditionsNotMetException("Film name is empty");
        }

        if (film.getDescription().length() >= maxDescriptionLength) {
            InMemoryFilmStorage.log.error("Description exceed 200 symbols");
            throw new ConditionsNotMetException("Description's length shouldn't exceed 200 symbols");
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))) {
            InMemoryFilmStorage.log.error("Release date {} is incorrect", film.getReleaseDate());
            throw new ConditionsNotMetException("Release date is incorrect");
        }

        if (film.getDuration() < 0) {
            InMemoryFilmStorage.log.error("Film's duration {} is incorrect", film.getDuration());
            throw new ConditionsNotMetException("Film's duration is incorrect");
        }
    }

    @Override
    public Film create(Film film) {
        validate(film);
        film.setId(idCounter.incrementAndGet());
        if (film.getUsersLikes() == null) {
            film.setUsersLikes(new HashSet<>());
        }
        films.put(film.getId(), film.toBuilder().build());
        log.debug("{} was created", film);
        return film;
    }

    public void checkIdExist(long id) {
        if (!films.containsKey(id)) {
            throw new NotFoundException("Film with id " + id + " not found");
        }
    }

}
