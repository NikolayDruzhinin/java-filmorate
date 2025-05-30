package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilmRqDto {
    @NotBlank(message = "Name can not be blanc")
    private String name;

    @Size(max = 200, message = "Description must be 200 characters or less")
    private String description;

    @NotNull
    @PastOrPresent(message = "Release date must be past or present")
    private LocalDate releaseDate;

    @Positive(message = "Duration must be positive")
    private int duration;

    private MpaRqDto mpa;

    private Set<GenreRqDto> genres;

    @AssertTrue(message = "Release date must be after December 28, 1895")
    public boolean isReleaseDateValid() {
        return releaseDate == null ||
                !releaseDate.isBefore(LocalDate.of(1895, 12, 28));
    }
}
