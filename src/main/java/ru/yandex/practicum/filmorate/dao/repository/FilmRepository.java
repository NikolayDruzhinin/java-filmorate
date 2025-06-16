package ru.yandex.practicum.filmorate.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.Film;

import java.util.List;

@Repository
public interface FilmRepository extends JpaRepository<Film, Long> {
    @Query("SELECT f FROM Film f " +
            "LEFT JOIN f.likedByUsers l " +
            "GROUP BY f " +
            "ORDER BY COUNT(l) DESC, f.id ASC " +
            "LIMIT :count")
    List<Film> findTopFilms(@Param("count") int count);
}