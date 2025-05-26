package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.Film;
import ru.yandex.practicum.filmorate.dao.Genre;
import ru.yandex.practicum.filmorate.dao.Mpa;
import ru.yandex.practicum.filmorate.dao.User;
import ru.yandex.practicum.filmorate.dao.repository.FilmRepository;
import ru.yandex.practicum.filmorate.dao.repository.MpaRepository;
import ru.yandex.practicum.filmorate.dao.repository.UserRepository;
import ru.yandex.practicum.filmorate.dto.FilmCreateDto;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final FilmRepository filmRepository;
    private final UserRepository userRepository;
    private final MpaRepository mpaRepository;
    private final ModelMapper modelMapper;

    public List<FilmDto> getAllFilms() {
        log.info("getting all films rq");
        return filmRepository.findAll().stream()
                .map(f -> modelMapper.map(f, FilmDto.class))
                .toList();
    }

    public FilmDto createFilm(FilmCreateDto filmCreateDto) {
        log.info("create film rq {}", filmCreateDto);
        Film film = modelMapper.map(filmCreateDto, Film.class);

        if (filmCreateDto.getMpa() != null) {
            Mpa mpa = mpaRepository.findById(filmCreateDto.getMpa().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("MPA with id " + filmCreateDto.getMpa().getId() + " not found"));
            film.setMpa(mpa);
        }
        Film saved = filmRepository.save(film);
        return modelMapper.map(saved, FilmDto.class);
    }

    public FilmDto updateFilm(FilmDto filmDto) {
        log.info("update film rq {}", filmDto);
        Film film = filmRepository.findById(filmDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Film " + filmDto + " not found"));
        modelMapper.map(filmDto, film);
        Film updated = filmRepository.save(film);
        return modelMapper.map(updated, FilmDto.class);
    }

    public List<FilmDto> getTopFilms(int count) {
        log.info("top {} film rq", count);
        return filmRepository.findTopFilms(count).stream()
                .map(f -> modelMapper.map(f, FilmDto.class))
                .collect(Collectors.toList());
    }

    public void addLike(Long filmId, Long userId) {
        log.info("add like for filmId {} by userId {}", filmId, userId);
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new ResourceNotFoundException("Film with id " + filmId + " not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));

        film.getLikedByUsers().add(user);
        filmRepository.save(film);
    }

    public void removeLike(Long filmId, Long userId) {
        log.info("remove like for filmId {} by userId {}", filmId, userId);
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new ResourceNotFoundException("Film with id " + filmId + " not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));

        film.getLikedByUsers().remove(user);
        filmRepository.save(film);
    }

    public Set<Genre> getFilmGenres(Long filmId) {
        log.info("film genres rq for filmId {}", filmId);
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new ResourceNotFoundException("Film with id " + filmId + " not found"));
        return film.getGenres();
    }
}
