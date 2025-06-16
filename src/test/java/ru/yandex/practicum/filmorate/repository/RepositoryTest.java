package ru.yandex.practicum.filmorate.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.yandex.practicum.filmorate.dao.Film;
import ru.yandex.practicum.filmorate.dao.Genre;
import ru.yandex.practicum.filmorate.dao.Mpa;
import ru.yandex.practicum.filmorate.dao.User;
import ru.yandex.practicum.filmorate.dao.repository.FilmRepository;
import ru.yandex.practicum.filmorate.dao.repository.GenreRepository;
import ru.yandex.practicum.filmorate.dao.repository.MpaRepository;
import ru.yandex.practicum.filmorate.dao.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RepositoryTest {

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private MpaRepository mpaRepository;

    @Autowired
    private EntityManager entityManager;

    private Film testFilm;
    private User testUser;
    private Genre testGenre;
    private Mpa testMpa;

    @BeforeAll
    void setUp() {
        mpaRepository.deleteAll();
        genreRepository.deleteAll();

        testMpa = new Mpa("NC-17");
        mpaRepository.save(testMpa);

        testGenre = new Genre("Боевик");
        genreRepository.save(testGenre);

        testUser = User.builder()
                .email("test@example.com")
                .login("testLogin")
                .name("Test User")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        userRepository.save(testUser);

        testFilm = Film.builder()
                .name("Test Film")
                .description("Test Description")
                .releaseDate(LocalDate.of(2020, 1, 1))
                .duration(120)
                .mpa(testMpa)
                .build();
        filmRepository.save(testFilm);
    }

    @Test
    public void testFilmCrudOperations() {
        Film newFilm = Film.builder()
                .name("New Film")
                .description("New Description")
                .releaseDate(LocalDate.now())
                .duration(90)
                .mpa(testMpa)
                .build();

        Film savedFilm = filmRepository.save(newFilm);
        assertNotNull(savedFilm.getId());

        Optional<Film> foundFilm = filmRepository.findById(savedFilm.getId());
        assertTrue(foundFilm.isPresent());
        assertEquals("New Film", foundFilm.get().getName());

        savedFilm.setName("Updated Title");
        Film updatedFilm = filmRepository.save(savedFilm);
        assertEquals("Updated Title", updatedFilm.getName());

        filmRepository.delete(updatedFilm);
        assertFalse(filmRepository.findById(updatedFilm.getId()).isPresent());
    }

    @Test
    public void testFindTopFilms() {
        testFilm.getLikedByUsers().add(testUser);
        filmRepository.save(testFilm);

        List<Film> topFilms = filmRepository.findTopFilms(1);
        assertEquals(1, topFilms.size());
        assertEquals(testFilm.getId(), topFilms.get(0).getId());
    }

    @Test
    public void testGenreCrudOperations() {
        Genre newGenre = new Genre("Драма");
        Genre savedGenre = genreRepository.save(newGenre);
        assertNotNull(savedGenre.getId());

        Optional<Genre> foundGenre = genreRepository.findById(savedGenre.getId());
        Assertions.assertTrue(foundGenre.isPresent());
        assertEquals("Драма", foundGenre.get().getName());

        savedGenre.setName("Боевик");
        Genre updatedGenre = genreRepository.save(savedGenre);
        assertEquals("Боевик", updatedGenre.getName());

        genreRepository.delete(updatedGenre);
        assertFalse(genreRepository.findById(updatedGenre.getId()).isPresent());
    }

    @Test
    public void testFindByGenreType() {
        Optional<Genre> foundGenre = genreRepository.findByName(testGenre.getName());
        Assertions.assertTrue(foundGenre.isPresent());
        assertEquals(testGenre.getId(), foundGenre.get().getId());
    }

    @Test
    public void testUserCrudOperations() {
        User newUser = User.builder()
                .email("new@example.com")
                .login("newLogin")
                .name("New User")
                .birthday(LocalDate.of(1995, 5, 5))
                .build();

        User savedUser = userRepository.save(newUser);
        assertNotNull(savedUser.getId());

        var foundUser = userRepository.findById(savedUser.getId());
        assertTrue(foundUser.isPresent());
        assertEquals("newLogin", foundUser.get().getLogin());

        savedUser.setName("Updated Name");
        User updatedUser = userRepository.save(savedUser);
        assertEquals("Updated Name", updatedUser.getName());

        userRepository.delete(updatedUser);
        assertFalse(userRepository.findById(updatedUser.getId()).isPresent());
    }

    @Test
    public void testFindByEmailAndLogin() {
        Optional<User> byEmail = userRepository.findByEmail("test@example.com");
        assertTrue(byEmail.isPresent());

        Optional<User> byLogin = userRepository.findByLogin("testLogin");
        assertTrue(byLogin.isPresent());
    }

    @Test
    public void testMpaCrudOperations() {
        Mpa newMpa = new Mpa("PG");
        Mpa savedMpa = mpaRepository.save(newMpa);
        assertNotNull(savedMpa.getId());

        Optional<Mpa> foundMpa = mpaRepository.findById(savedMpa.getId());
        assertTrue(foundMpa.isPresent());
        assertEquals("PG", foundMpa.get().getName());

        savedMpa.setName("R");
        Mpa updatedMpa = mpaRepository.save(savedMpa);
        assertEquals("R", updatedMpa.getName());

        mpaRepository.delete(updatedMpa);
        assertFalse(mpaRepository.findById(updatedMpa.getId()).isPresent());
    }

    @Test
    public void testFilmGenreRelationship() {
        testFilm.addGenre(testGenre);
        filmRepository.saveAndFlush(testFilm);
        entityManager.clear();

        Film savedFilm = filmRepository.findById(testFilm.getId()).get();
        Genre savedGenre = genreRepository.findById(testGenre.getId()).get();

        assertEquals(1, savedFilm.getGenres().size());
        assertEquals(1, savedGenre.getFilms().size());
        assertTrue(savedGenre.getFilms().contains(savedFilm));
    }

    @Test
    public void testUserFriendRelationship() {
        User friend = User.builder()
                .email("friend@example.com")
                .login("friendLogin")
                .name("Friend User")
                .birthday(LocalDate.of(1992, 2, 2))
                .build();
        userRepository.save(friend);

        testUser.getFriends().add(friend);
        userRepository.save(testUser);

        User userWithFriends = userRepository.findById(testUser.getId()).get();
        assertEquals(1, userWithFriends.getFriends().size());
        assertTrue(userWithFriends.getFriends().contains(friend));
    }
}
