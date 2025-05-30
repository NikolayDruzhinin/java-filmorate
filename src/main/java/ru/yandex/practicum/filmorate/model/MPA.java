package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum MPA {
    G, PG, PG_13, R, NC_17;

    @JsonValue
    public String toValue() {
        return name().replace('_', '-');
    }
}
