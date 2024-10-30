package ru.practicum.shareit.user;

import java.util.List;

public interface UserService {
    User createUser(User user);
    User updateUser(Long id, User user);
    User getUser(Long id);
    boolean deleteUser(Long id);
}
