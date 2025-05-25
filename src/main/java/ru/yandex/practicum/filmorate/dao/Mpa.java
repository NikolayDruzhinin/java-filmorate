package ru.yandex.practicum.filmorate.dao;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.model.MPA;

@Entity
@Table(name = "mpa")
@Data
@NoArgsConstructor
public class Mpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    private MPA name;

    public Mpa(MPA name) {
        this.name = name;
    }
}
