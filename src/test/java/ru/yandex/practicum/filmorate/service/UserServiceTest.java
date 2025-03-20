package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserServiceTest {
    private UserService userService;
    private User user1;

    @BeforeEach
    public void init() {
        userService = new UserService();

        user1 = User.builder()
                .login("test")
                .email("test@ru")
                .name("test")
                .birthday(LocalDate.now())
                .build();
    }

    @Test
    public void user_create_should_be_valid() {
        userService.create(user1);
        assertEquals(userService.get().stream().findFirst().get(), user1);
    }

    @Test
    public void user_update_should_be_valid() {
        userService.create(user1);
        User user2 = user1.toBuilder()
                .login("test2")
                .email("test2@ru")
                .build();
        userService.update(user2);
        assertEquals(userService.get().stream().findFirst().get(), user2);
    }

    @Test
    public void user_create_should_throw_ConditionsNotMetException() {
        user1.setEmail("");
        assertThrows(ConditionsNotMetException.class, () -> userService.create(user1));
        user1.setEmail("test.ru");
        assertThrows(ConditionsNotMetException.class, () -> userService.create(user1));
        user1.setBirthday(LocalDate.now().plusYears(1));
        assertThrows(ConditionsNotMetException.class, () -> userService.create(user1));
    }

    @Test
    public void user_create_should_throw_NotFoundException() {
        user1.setLogin("");
        assertThrows(NotFoundException.class, () -> userService.create(user1));
    }

    @Test
    public void user_update_should_throw_NotFoundException() {
        user1.setId(2);
        assertThrows(NotFoundException.class, () -> userService.update(user1));
    }
}
