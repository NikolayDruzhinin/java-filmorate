package ru.yandex.practicum.filmorate.util;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.filmorate.dao.Film;
import ru.yandex.practicum.filmorate.dao.User;
import ru.yandex.practicum.filmorate.dto.FilmRqDto;
import ru.yandex.practicum.filmorate.dto.UserRqDto;

@Configuration
public class MapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        mapper.typeMap(FilmRqDto.class, Film.class)
                .addMappings(m -> m.skip(Film::setId));

        mapper.typeMap(UserRqDto.class, User.class)
                .addMappings(m -> m.skip(User::setId));

        return mapper;
    }
}
