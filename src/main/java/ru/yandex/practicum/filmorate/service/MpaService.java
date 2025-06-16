package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.Mpa;
import ru.yandex.practicum.filmorate.dao.repository.MpaRepository;
import ru.yandex.practicum.filmorate.dto.MpaRsDto;
import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MpaService {
    private final MpaRepository mpaRepository;
    private final ModelMapper modelMapper;

    public List<MpaRsDto> getAll() {
        return mpaRepository.findAllByOrderByIdAsc().stream()
                .map(mpa -> modelMapper.map(mpa, MpaRsDto.class))
                .toList();
    }

    public MpaRsDto getMpaById(Long mpaId) {
        log.info("map rq for mpaId {}", mpaId);
        Mpa mpa = mpaRepository.findById(mpaId)
                .orElseThrow(() -> new ResourceNotFoundException("Mpa " + mpaId + "not found"));
        return modelMapper.map(mpa, MpaRsDto.class);
    }
}
