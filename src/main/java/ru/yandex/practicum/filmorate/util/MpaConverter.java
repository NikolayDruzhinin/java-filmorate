package ru.yandex.practicum.filmorate.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.yandex.practicum.filmorate.model.MPA;

@Converter(autoApply = true)
public class MpaConverter implements AttributeConverter<MPA, String> {
    @Override
    public String convertToDatabaseColumn(MPA mpa) {
        if (mpa == null) return null;
        return mpa.name().replace("_", "-");
    }

    @Override
    public MPA convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return MPA.valueOf(dbData.replace("-", "_"));
    }
}
