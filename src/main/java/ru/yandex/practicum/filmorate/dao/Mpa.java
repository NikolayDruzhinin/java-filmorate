package ru.yandex.practicum.filmorate.dao;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mpa")
@Data
@NoArgsConstructor
public class Mpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    public Mpa(String name) {
        this.name = name;
    }
}
