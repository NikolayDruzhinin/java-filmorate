package ru.yandex.practicum.filmorate.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.Genre;

import java.util.List;
import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    Optional<Genre> findById(Long id);

    Optional<Genre> findByName(String name);

    List<Genre> findAllByOrderByIdAsc();

}
