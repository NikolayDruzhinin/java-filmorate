package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InMemoryFilmStorageTest {
    private InMemoryFilmStorage inMemoryFilmStorage;
    private Film film1;

    @BeforeEach
    public void init() {
        inMemoryFilmStorage = new InMemoryFilmStorage();
        film1 = Film.builder()
                .name("test")
                .description("description")
                .releaseDate(LocalDate.now())
                .duration(120)
                .build();
    }

    @Test
    public void film_create_should_be_valid() {
        inMemoryFilmStorage.create(film1);
        assertEquals(inMemoryFilmStorage.get().stream().findFirst().get(), film1);
    }

    @Test
    public void film_update_should_be_valid() {
        inMemoryFilmStorage.create(film1);
        Film film2 = film1.toBuilder()
                .name("New name")
                .releaseDate(LocalDate.now().minusYears(1))
                .description("New description")
                .duration(1)
                .build();
        inMemoryFilmStorage.update(film2);
        assertEquals(inMemoryFilmStorage.get().stream().findFirst().get(), film2);
    }

    @Test
    public void film_update_should_throw_NotFoundException() {
        assertThrows(NotFoundException.class, () -> inMemoryFilmStorage.update(film1));
    }

    @Test
    public void film_create_should_throw_ConditionsNotMetException() {
        film1.setName("");
        assertThrows(ConditionsNotMetException.class, () -> inMemoryFilmStorage.create(film1));

        film1.setName(null);
        assertThrows(ConditionsNotMetException.class, () -> inMemoryFilmStorage.create(film1));

        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWSYZ";
        StringBuilder sb = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < 200; ++i) {
            sb.append(alphabet.charAt(Math.abs(rand.nextInt()) % alphabet.length()));
        }

        film1.setDescription(sb.toString());
        assertThrows(ConditionsNotMetException.class, () -> inMemoryFilmStorage.create(film1));

        film1.setReleaseDate(LocalDate.of(1984, Month.DECEMBER, 28));
        assertThrows(ConditionsNotMetException.class, () -> inMemoryFilmStorage.create(film1));

        film1.setDuration(-1);
        assertThrows(ConditionsNotMetException.class, () -> inMemoryFilmStorage.create(film1));
    }

}
