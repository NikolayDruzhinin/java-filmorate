package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.Mpa;
import ru.yandex.practicum.filmorate.dao.repository.MpaRepository;
import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MpaService {
    private final MpaRepository mpaRepository;

    public List<Mpa> getAll() {
        return mpaRepository.findAll();
    }

    public Mpa getMpaById(Long mpaId) {
        log.info("map rq for mpaId {}", mpaId);
        return mpaRepository.findById(mpaId)
                .orElseThrow(() -> new ResourceNotFoundException("Mpa " + mpaId + "not found"));
    }
}
