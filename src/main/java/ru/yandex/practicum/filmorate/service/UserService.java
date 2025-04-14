package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {
    private final Storage<User> userStorage;

    public void addFriend(long id, long friendId) {
        userStorage.checkIdExist(id);
        userStorage.checkIdExist(friendId);

        if (userStorage.get(id).getFriends().contains(friendId)) {
            throw new ConditionsNotMetException("User with id " + id
                    + " has already have friend with id " + friendId);
        }

        userStorage.get(id).getFriends().add(friendId);
        userStorage.get(friendId).getFriends().add(id);
    }

    public void deleteFriend(long id, long friendId) {
        userStorage.checkIdExist(id);
        userStorage.checkIdExist(friendId);

        userStorage.get(id).getFriends().remove(friendId);
        userStorage.get(friendId).getFriends().remove(id);
    }

    public Collection<User> getFriends(long id) {
        userStorage.checkIdExist(id);
        Set<Long> friendsIds = userStorage.get(id).getFriends();
        return userStorage.get().stream()
                .filter(user -> friendsIds.contains(user.getId()))
                .collect(Collectors.toSet());
    }

    public Collection<User> getCommonFriends(long id, long otherId) {
        userStorage.checkIdExist(id);
        userStorage.checkIdExist(otherId);

        Set<Long> userFriendsIds = userStorage.get(id).getFriends();
        Set<Long> otherUserFriendsIds = userStorage.get(otherId).getFriends();
        userFriendsIds.retainAll(otherUserFriendsIds);
        return userStorage.get().stream()
                .filter(user -> userFriendsIds.contains(user.getId()))
                .collect(Collectors.toSet());
    }

}
