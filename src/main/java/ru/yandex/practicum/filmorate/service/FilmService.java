package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
@Slf4j
public class FilmService {
    private final Map<Long, Film> films = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong(0);
    private final int DESCRIPTION_LENGTH = 200;

    public Collection<Film> get() {
        return films.values();
    }

    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            log.error("Film with id {} not found", film.getId());
            throw new NotFoundException("Film with id " + film.getId() + " not found");
        }

        Film oldFilm = films.get(film.getId());
        oldFilm.setDescription(film.getDescription());
        oldFilm.setName(film.getName());
        oldFilm.setDuration(film.getDuration());
        oldFilm.setReleaseDate(film.getReleaseDate());
        log.debug("{} was updated", film);
        return film;
    }

    public Film create(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.error("Film name is empty");
            throw new ConditionsNotMetException("Film name is empty");
        }

        if (film.getDescription().length() >= DESCRIPTION_LENGTH) {
            log.error("Description exceed 200 symbols");
            throw new ConditionsNotMetException("Description's length shouldn't exceed 200 symbols");
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))) {
            log.error("Release date {} is incorrect", film.getReleaseDate());
            throw new ConditionsNotMetException("Release date is incorrect");
        }

        if (film.getDuration() < 0) {
            log.error("Film's duration {} is incorrect", film.getDuration());
            throw new ConditionsNotMetException("Film's duration is incorrect");
        }

        film.setId(idCounter.incrementAndGet());
        films.put(film.getId(), film.toBuilder().build());
        log.debug("{} was created", film);
        return film;
    }
}
