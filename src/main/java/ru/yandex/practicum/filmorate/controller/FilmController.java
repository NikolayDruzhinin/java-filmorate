package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmRqDto;
import ru.yandex.practicum.filmorate.dto.FilmRsDto;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public Collection<FilmRsDto> getAll() {
        return filmService.getAllFilms();
    }

    @GetMapping("/{filmId}")
    public FilmRsDto getFilm(@PathVariable Long filmId) {
        return filmService.getFilmById(filmId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FilmRsDto create(@Valid @RequestBody FilmRqDto filmRequest) {
        return filmService.createFilm(filmRequest);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public FilmRsDto update(@Valid @RequestBody FilmRsDto film) {
        return filmService.updateFilm(film);
    }

    @PutMapping("/{filmId}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void makeLike(@Positive @PathVariable Long filmId,
                         @Positive @PathVariable Long userId) {
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void removeLike(@Positive @PathVariable Long filmId,
                           @Positive @PathVariable Long userId) {
        filmService.removeLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<FilmRsDto> getTopFilms(@RequestParam(defaultValue = "10") int count) {
        return filmService.getTopFilms(count);
    }

}
