package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * Film.
 */
@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Film {
    @EqualsAndHashCode.Include
    private long id;
    @NotNull
    @NotBlank
    private String title;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private Set<Long> usersLikes;
    private List<GENRE> genres;
    private MPA mpa;
}
