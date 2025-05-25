package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.Film;
import ru.yandex.practicum.filmorate.dao.Genre;
import ru.yandex.practicum.filmorate.dao.User;
import ru.yandex.practicum.filmorate.dao.repository.FilmRepository;
import ru.yandex.practicum.filmorate.dao.repository.UserRepository;
import ru.yandex.practicum.filmorate.dto.FilmCreateDto;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmRepository filmRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public List<FilmDto> getAllFilms() {
        return filmRepository.findAll().stream()
                .map(f -> modelMapper.map(f, FilmDto.class))
                .toList();
    }

    public FilmDto createFilm(FilmCreateDto filmCreateDto) {
        Film film = modelMapper.map(filmCreateDto, Film.class);
        Film saved = filmRepository.save(film);
        return modelMapper.map(saved, FilmDto.class);
    }

    public FilmDto updateFilm(FilmDto filmDto) {
        Film film = filmRepository.findById(filmDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Film not found"));
        modelMapper.map(filmDto, film);
        Film updated = filmRepository.save(film);
        return modelMapper.map(updated, FilmDto.class);
    }

    public List<FilmDto> getTopFilms(int count) {
        return filmRepository.findTopFilms(count).stream()
                .map(f -> modelMapper.map(f, FilmDto.class))
                .collect(Collectors.toList());
    }

    public void addLike(Long filmId, Long userId) {
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new ResourceNotFoundException("Film not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        film.getLikedByUsers().add(user);
        filmRepository.save(film);
    }

    public void removeLike(Long filmId, Long userId) {
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new ResourceNotFoundException("Film not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        film.getLikedByUsers().remove(user);
        filmRepository.save(film);
    }

    public Set<Genre> getFilmGenres(Long filmId) {
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new ResourceNotFoundException("Film not found"));
        return film.getGenres();
    }
}
