package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.Genre;
import ru.yandex.practicum.filmorate.dao.repository.GenreRepository;
import ru.yandex.practicum.filmorate.dto.GenreRsDto;
import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenreService {
    private final GenreRepository genreRepository;
    private final ModelMapper modelMapper;

    public List<GenreRsDto> getAll() {
        log.info("Get all genres request");
        return genreRepository.findAllByOrderByIdAsc().stream()
                .map(genre -> modelMapper.map(genre, GenreRsDto.class))
                .toList();
    }

    public GenreRsDto getGenreById(Long genreId) {
        log.info("genre rq for genreId {}", genreId);
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found"));
        return modelMapper.map(genre, GenreRsDto.class);
    }
}
