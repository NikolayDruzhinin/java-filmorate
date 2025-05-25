package ru.yandex.practicum.filmorate.dao;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Email
    @Column(unique = true)
    private String email;

    @NotBlank
    @Column(unique = true)
    private String login;

    private String name;

    @Past
    private LocalDate birthday;

    @ManyToMany(mappedBy = "likedByUsers")
    @Builder.Default
    private Set<Film> likedFilms = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )

    @Builder.Default
    private Set<User> friends = new HashSet<>();

    public User() {
    }

    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public void likeFilm(Film film) {
        likedFilms.add(film);
        film.getLikedByUsers().add(this);
    }

    public void unlikeFilm(Film film) {
        likedFilms.remove(film);
        film.getLikedByUsers().remove(this);
    }

    public void addFriend(User friend) {
        friends.add(friend);
        friend.getFriends().add(this);
    }

    public void removeFriend(User friend) {
        friends.remove(friend);
        friend.getFriends().remove(this);
    }
}
