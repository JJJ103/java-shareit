package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public interface UserService {
    User createUser(UserDto userDto);

    User updateUser(Long id, UserDto userDto);

    User getUser(Long id);

    boolean deleteUser(Long id);
}
