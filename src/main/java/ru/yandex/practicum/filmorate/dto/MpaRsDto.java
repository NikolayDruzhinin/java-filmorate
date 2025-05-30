package ru.yandex.practicum.filmorate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.model.MPA;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MpaRsDto {
    private Long id;
    private MPA name;
}
