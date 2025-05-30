package ru.yandex.practicum.filmorate.dao;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Setter;
import ru.yandex.practicum.filmorate.model.GENRE;
import ru.yandex.practicum.filmorate.util.GenreConverter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "genres")
@Data
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(name = "name", unique = true)
    @Convert(converter = GenreConverter.class)
    private GENRE name;

    @ManyToMany(mappedBy = "genres")
    private Set<Film> films = new HashSet<>();

    public Genre() {
    }

    public Genre(GENRE name) {
        this.name = name;
    }

    public void addFilm(Film film) {
        films.add(film);
        film.getGenres().add(this);
    }

    public void removeFilm(Film film) {
        films.remove(film);
        film.getGenres().remove(this);
    }
}
