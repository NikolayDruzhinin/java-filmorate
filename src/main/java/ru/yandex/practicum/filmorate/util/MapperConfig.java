package ru.yandex.practicum.filmorate.util;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.filmorate.dao.Film;
import ru.yandex.practicum.filmorate.dao.User;
import ru.yandex.practicum.filmorate.dto.FilmCreateDto;
import ru.yandex.practicum.filmorate.dto.UserCreateDto;

@Configuration
public class MapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        mapper.typeMap(FilmCreateDto.class, Film.class)
                .addMappings(m -> m.skip(Film::setId));

        mapper.typeMap(UserCreateDto.class, User.class)
                .addMappings(m -> m.skip(User::setId));

        return mapper;
    }
}
