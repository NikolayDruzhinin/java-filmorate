package ru.yandex.practicum.filmorate.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.yandex.practicum.filmorate.model.GENRE;

@Converter(autoApply = true)
public class GenreConverter implements AttributeConverter<GENRE, String> {

    @Override
    public String convertToDatabaseColumn(GENRE genre) {
        if (genre == null) {
            return null;
        }
        return genre.getValue(); // Returns "Драма" etc.
    }

    @Override
    public GENRE convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return GENRE.fromString(dbData); // Uses your existing fromString method
    }
}
