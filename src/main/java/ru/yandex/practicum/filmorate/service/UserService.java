package ru.yandex.practicum.filmorate.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.User;
import ru.yandex.practicum.filmorate.dao.repository.UserRepository;
import ru.yandex.practicum.filmorate.dto.UserRqDto;
import ru.yandex.practicum.filmorate.dto.UserRsDto;
import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public List<UserRsDto> getAllUsers() {
        log.info("get all users rq");
        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserRsDto.class))
                .toList();
    }

    public UserRsDto getUserById(Long userId) {
        log.info("get user rq by userId {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));
        return modelMapper.map(user, UserRsDto.class);
    }

    @Transactional
    public UserRsDto createUser(UserRqDto userCreateDto) {
        log.debug("create user rq {}", userCreateDto);
        User user = modelMapper.map(userCreateDto, User.class);
        User saved = userRepository.save(user);
        return modelMapper.map(saved, UserRsDto.class);
    }

    @Transactional
    public UserRsDto updateUser(UserRsDto userDto) {
        log.info("update user rq {}", userDto);
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User " + userDto + " not found"));
        modelMapper.map(userDto, user);
        User updated = userRepository.save(user);
        return modelMapper.map(updated, UserRsDto.class);
    }

    @Transactional
    public void addFriend(Long userId, Long friendId) {
        log.info("add friend for userId {} and friendId {}", userId, friendId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));
        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new ResourceNotFoundException("Friend with id " + friendId + "not found"));

        user.getFriends().add(friend);
        userRepository.save(user);
    }

    @Transactional
    public void removeFriend(Long userId, Long friendId) {
        log.info("remove friend for userId {} and friendId {}", userId, friendId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));
        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new ResourceNotFoundException("Friend with id " + friendId + " not found"));

        user.getFriends().remove(friend);
        userRepository.save(user);
    }

    public List<UserRsDto> getCommonFriends(Long userId, Long otherUserId) {
        log.info("get common friends rq for userId {} and otherUserId {}", userId, otherUserId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));
        User otherUser = userRepository.findById(otherUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + otherUserId + " not found"));

        Set<User> commonFriends = user.getFriends();
        commonFriends.retainAll(otherUser.getFriends());

        return commonFriends.stream()
                .map(u -> modelMapper.map(u, UserRsDto.class))
                .collect(Collectors.toList());
    }

    public List<UserRsDto> getUserFriends(Long userId) {
        log.info("get user friends for userId {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));
        return user.getFriends().stream()
                .map(u -> modelMapper.map(u, UserRsDto.class))
                .toList();
    }

}
