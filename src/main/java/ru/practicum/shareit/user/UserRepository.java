package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository {
    private final List<User> users = new ArrayList<>();
    private long userIdCounter = 1;

    public User createUser(User user) {
        user.setId(userIdCounter++);
        users.add(user);
        return user;
    }

    public User updateUser(Long id, User user) {
        User existingUser = findUserById(id);
        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        return existingUser;
    }

    public User getUser(Long id) {
        return findUserById(id);
    }

    public boolean deleteUser(Long id) {
        return users.removeIf(user -> user.getId().equals(id));
    }

    private User findUserById(Long id) {
        return users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    protected boolean existsByEmail(String email) {
        return users.stream()
                .filter(user -> user.getEmail() != null)
                .anyMatch(user -> user.getEmail().equals(email));
    }
}
