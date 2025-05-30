package ru.yandex.practicum.filmorate.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GenreRqDto {
    private Long id;

    public GenreRqDto(Long id) {
        this.id = id;
    }
}
