package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.User;
import ru.yandex.practicum.filmorate.dao.repository.UserRepository;
import ru.yandex.practicum.filmorate.dto.UserCreateDto;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .toList();
    }

    public UserDto getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User " + userId + " not found"));
        return modelMapper.map(user, UserDto.class);
    }

    public UserDto createUser(UserCreateDto userCreateDto) {
        User user = modelMapper.map(userCreateDto, User.class);
        User saved = userRepository.save(user);
        return modelMapper.map(saved, UserDto.class);
    }

    public UserDto updateUser(UserDto userDto) {
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User " + userDto.getId() + " not found"));
        modelMapper.map(userDto, user);
        User updated = userRepository.save(user);
        return modelMapper.map(updated, UserDto.class);
    }

    public void addFriend(Long userId, Long friendId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User " + userId + " not found"));
        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new ResourceNotFoundException("Friend " + friendId + "not found"));

        user.getFriends().add(friend);
        userRepository.save(user);
    }

    public void removeFriend(Long userId, Long friendId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User " + userId + " not found"));
        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new ResourceNotFoundException("Friend " + friendId + " not found"));

        user.getFriends().remove(friend);
        userRepository.save(user);
    }

    public List<UserDto> getCommonFriends(Long userId, Long otherUserId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User " + userId + " not found"));
        User otherUser = userRepository.findById(otherUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User " + otherUserId + " not found"));

        Set<User> commonFriends = user.getFriends();
        commonFriends.retainAll(otherUser.getFriends());

        return commonFriends.stream()
                .map(u -> modelMapper.map(u, UserDto.class))
                .collect(Collectors.toList());
    }

    public List<UserDto> getUserFriends(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User " + userId + " not found"));
        return user.getFriends().stream()
                .map(u -> modelMapper.map(u, UserDto.class))
                .toList();
    }

}
