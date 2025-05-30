package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum GENRE {
    COMEDY("Комедия"),
    DRAMA("Драма"),
    THRILLER("Триллер"),
    DOCUMENTARY("Документальный"),
    ACTION("Боевик"),
    ANIMATION("Мультфильм");

    private String value;

    GENRE(String genre) {
        value = genre;
    }

    @JsonValue
    public String toValue() {
        return value;
    }

    @JsonCreator
    public static GENRE forValue(String value) {
        return fromString(value);
    }

    public static GENRE fromString(String value) {
        for (GENRE genre : GENRE.values()) {
            if (genre.value.equalsIgnoreCase(value)) {
                return genre;
            }
        }
        throw new IllegalArgumentException("Unknown genre: " + value);
    }

}
