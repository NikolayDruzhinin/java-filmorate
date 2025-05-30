package ru.yandex.practicum.filmorate.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.Film;
import ru.yandex.practicum.filmorate.dao.Genre;
import ru.yandex.practicum.filmorate.dao.Mpa;
import ru.yandex.practicum.filmorate.dao.User;
import ru.yandex.practicum.filmorate.dao.repository.FilmRepository;
import ru.yandex.practicum.filmorate.dao.repository.GenreRepository;
import ru.yandex.practicum.filmorate.dao.repository.MpaRepository;
import ru.yandex.practicum.filmorate.dao.repository.UserRepository;
import ru.yandex.practicum.filmorate.dto.FilmRqDto;
import ru.yandex.practicum.filmorate.dto.FilmRsDto;
import ru.yandex.practicum.filmorate.dto.GenreRqDto;
import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final FilmRepository filmRepository;
    private final UserRepository userRepository;
    private final MpaRepository mpaRepository;
    private final GenreRepository genreRepository;
    private final ModelMapper modelMapper;

    public List<FilmRsDto> getAllFilms() {
        log.info("getting all films rq");
        return filmRepository.findAll().stream()
                .map(f -> modelMapper.map(f, FilmRsDto.class))
                .toList();
    }

    @Transactional
    public FilmRsDto createFilm(FilmRqDto filmRequest) {
        log.info("create film rq {}", filmRequest);
        Film film = modelMapper.map(filmRequest, Film.class);

        if (filmRequest.getMpa() != null) {
            Mpa mpa = mpaRepository.findById(filmRequest.getMpa().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("MPA with id " + filmRequest.getMpa().getId() + " not found"));
            film.setMpa(mpa);
        }

        if (filmRequest.getGenres() != null) {
            List<Genre> genres = filmRequest.getGenres().stream()
                    .sorted(Comparator.comparingLong(GenreRqDto::getId))
                    .map(genre -> genreRepository.findById(genre.getId())
                            .orElseThrow(() -> new ResourceNotFoundException("Genre with id " + genre.getId() + " not found")))
                    .toList();
            film.setGenres(genres);
        }

        Film saved = filmRepository.save(film);
        return modelMapper.map(saved, FilmRsDto.class);
    }

    @Transactional
    public FilmRsDto updateFilm(FilmRsDto filmDto) {
        log.info("update film rq {}", filmDto);
        Film film = filmRepository.findById(filmDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Film " + filmDto + " not found"));
        film.setName(filmDto.getName());
        film.setDescription(filmDto.getDescription());
        film.setReleaseDate(filmDto.getReleaseDate());
        film.setDuration(filmDto.getDuration());

        if (filmDto.getMpa() != null && filmDto.getMpa().getId() != null) {
            Mpa newMpa = mpaRepository.findById(filmDto.getMpa().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("MPA not found"));
            film.setMpa(newMpa);
        }
        filmRepository.save(film);
        return filmDto;
    }

    public List<FilmRsDto> getTopFilms(int count) {
        log.info("top {} film rq", count);
        return filmRepository.findTopFilms(count).stream()
                .map(f -> modelMapper.map(f, FilmRsDto.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public void addLike(Long filmId, Long userId) {
        log.info("add like for filmId {} by userId {}", filmId, userId);
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new ResourceNotFoundException("Film with id " + filmId + " not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));

        film.getLikedByUsers().add(user);
        filmRepository.save(film);
    }

    @Transactional
    public void removeLike(Long filmId, Long userId) {
        log.info("remove like for filmId {} by userId {}", filmId, userId);
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new ResourceNotFoundException("Film with id " + filmId + " not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));

        film.getLikedByUsers().remove(user);
        filmRepository.save(film);
    }

    public FilmRsDto getFilmById(Long filmId) {
        log.info("film genres rq for filmId {}", filmId);
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new ResourceNotFoundException("Film with id " + filmId + " not found"));
        return modelMapper.map(film, FilmRsDto.class);
    }
}
