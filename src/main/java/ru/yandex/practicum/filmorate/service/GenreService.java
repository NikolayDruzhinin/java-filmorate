package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.Genre;
import ru.yandex.practicum.filmorate.dao.repository.GenreRepository;
import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;

    public List<Genre> getAll() {
        return genreRepository.findAll();
    }

    public Genre getGenreById(Long genreId) {
        return genreRepository.findById(genreId)
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found"));
    }
}
