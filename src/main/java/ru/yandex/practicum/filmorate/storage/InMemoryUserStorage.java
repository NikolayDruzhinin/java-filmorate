package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Component
@Slf4j
public class InMemoryUserStorage implements Storage<User> {
    private final Map<Long, User> users = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong(0);

    public Collection<User> get() {
        return users.values();
    }

    public User get(long id) {
        return users.get(id);
    }

    public User update(User user) {
        if (!users.containsKey(user.getId())) {
            log.error("User with id {} not found", user.getId());
            throw new NotFoundException("User with id " + user.getId() + " not found");
        }

        validate(user);

        User oldUser = users.get(user.getId());
        oldUser.setName(user.getName());
        oldUser.setLogin(user.getLogin());
        oldUser.setEmail(user.getEmail());
        oldUser.setBirthday(user.getBirthday());
        if (user.getFriends() != null) {
            oldUser.setFriends(user.getFriends());
        }
        return user;
    }

    public User create(User user) {
        validate(user);

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        user.setId(idCounter.incrementAndGet());
        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }
        users.put(user.getId(), user.toBuilder().build());
        log.debug("{} was created", user);
        return user;
    }

    private void validate(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.error("Email '{}' is incorrect", user.getEmail());
            throw new ConditionsNotMetException("Email is incorrect");
        }
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            log.error("User login is empty");
            throw new NotFoundException("User login is empty");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Birthday {} is incorrect", user.getBirthday());
            throw new ConditionsNotMetException("Birthday is incorrect");
        }
    }

    public void checkIdExist(long id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("User with id " + id + " not found");
        }
    }
}
