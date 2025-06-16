package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.UserRqDto;
import ru.yandex.practicum.filmorate.dto.UserRsDto;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserRsDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserRsDto getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void addFriend(@Positive @PathVariable Long id,
                          @Positive @PathVariable Long friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void removeFriend(@Positive @PathVariable Long id,
                             @Positive @PathVariable Long friendId) {
        userService.removeFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<UserRsDto> getFriends(@Positive @PathVariable Long id) {
        return userService.getUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<UserRsDto> getCommonFriends(@Positive @PathVariable Long id,
                                            @Positive @PathVariable Long otherId) {
        return userService.getCommonFriends(id, otherId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserRsDto create(@Valid @RequestBody UserRqDto userRequest) {
        return userService.createUser(userRequest);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public UserRsDto update(@Valid @RequestBody UserRsDto user) {
        return userService.updateUser(user);
    }
}
